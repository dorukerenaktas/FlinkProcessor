package test.processor.cep;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import test.processor.pattern.PatternProvider;
import test.processor.pattern.EventCEPPattern;
import test.processor.pattern.EventPatternSelectFunction;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;
import org.apache.flink.streaming.util.serialization.JSONDeserializationSchema;
import org.apache.flink.util.Collector;

public class FlinkCEProcessor {

	private final static Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private StreamExecutionEnvironment environment;

	public FlinkCEProcessor(PatternProvider provider, String broker, String eventTopic, String actionTopic) {
		LOG.debug("Initializing Complex Event Proccessor...");
		this.environment = StreamExecutionEnvironment.getExecutionEnvironment();

		Properties properties = new Properties();
		properties.setProperty("bootstrap.servers", broker);

		DataStream<ObjectNode> inputStream = this.environment
				.addSource(new FlinkKafkaConsumer011<>(eventTopic, new JSONDeserializationSchema(), properties));

		DataStream<EventCEPData> parsedStream = inputStream.flatMap(new FlatMapFunction<ObjectNode, EventCEPData>() {

			private static final long serialVersionUID = -9108155491318459397L;

			@Override
			public void flatMap(ObjectNode input, Collector<EventCEPData> out) throws Exception {
				Long order = input.get("order").asLong();
				String company = input.get("company").asText();
				String product = input.get("product").asText();
				Long price = input.get("price").asLong();

				EventCEPData data = new EventCEPData(order, company, product, price);

				LOG.debug("Received data: " + data.toString());
				out.collect(data);
			}
		});

		ArrayList<EventCEPPattern> patterns = provider.provide();
		for (EventCEPPattern pattern : patterns) {
			
			DataStream<EventCEPData> reducedStream = parsedStream
					.keyBy(0, 1)
					.timeWindow(Time.milliseconds(pattern.getRule().getTime()))
					.reduce(new ReduceFunction<EventCEPData>() {
						private static final long serialVersionUID = 5589978320390489695L;
		
						@Override
						public EventCEPData reduce(EventCEPData first, EventCEPData second) throws Exception {
							first.setProduct(first.getProduct() + "," + second.getProduct());
							return first;
						}});
			
			PatternStream<EventCEPData> patternStream = CEP.pattern(reducedStream, pattern.getPattern());
			try {
				patternStream.select(new EventPatternSelectFunction(pattern.getRule(), broker, actionTopic));
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
	}

	public void process() {
		try {
			this.environment.execute();
			LOG.debug("Complex Event Proccessor started!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
