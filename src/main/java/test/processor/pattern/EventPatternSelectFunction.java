package test.processor.pattern;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.flink.cep.PatternSelectFunction;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import test.processor.cep.EventCEPData;
import test.processor.rule.Rule;

public class EventPatternSelectFunction implements PatternSelectFunction<EventCEPData, Boolean> {

	private final static Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private static final long serialVersionUID = 3709818359563691696L;

	private Rule rule;
	
	private String broker;

	private String topic;

	public EventPatternSelectFunction(Rule rule, String broker, String topic)
			throws Exception {
		this.rule = rule;
		this.broker = broker;
		this.topic = topic;
	}

	@Override
	public Boolean select(Map<String, List<EventCEPData>> patterns) throws Exception {
		Properties props = new Properties();
		props.put("bootstrap.servers", this.broker);
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.connect.json.JsonSerializer");
		
		Producer<String, JsonNode> producer = new KafkaProducer<String, JsonNode>(props);
		
		for (Entry<String, List<EventCEPData>> pattern : patterns.entrySet()) {
		    List<EventCEPData> list = pattern.getValue();
		    
		    for (EventCEPData data: list) {
				ObjectMapper objectMapper = new ObjectMapper();
				JsonNode objNode = objectMapper.readTree(this.rule.toJson().toString());

				ProducerRecord<String, JsonNode> record = new ProducerRecord<String, JsonNode>(
						this.topic, "action", objNode);
				producer.send(record);
				LOG.debug("Action send: " + objNode.toString());
		    }
		}
		
		producer.close();
		return true;
	}

}
