package test.generator;

import java.lang.invoke.MethodHandles;
import java.util.Properties;
import java.util.Random;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SampleDataGenerator {
	private final static Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private final String[] companies = {"EBAY", "AMAZON", "BIGCOMMERCE", "WEEBLY"};
	private final String[] products = {"STRAWBERRY", "TOMATO", "TEA", "PAPER", "WATER", "BOX", "CARD", "BAG", "CAKE", "BUTTER"};
	
	private String topic;
	
	private Producer<String, JsonNode> producer;
	
	public SampleDataGenerator(String broker, String topic) throws Exception {
		this.topic = topic;
		Properties props = new Properties();
		props.put("bootstrap.servers", broker);
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.connect.json.JsonSerializer");
		this.producer = new KafkaProducer<String, JsonNode>(props);
	}

	public void generate() throws Exception {
		Random rand = new Random();
		
		JSONObject json = new JSONObject();
		json.put("order", rand.nextInt(4));
    	json.put("company", this.companies[rand.nextInt(this.companies.length)]);
    	json.put("product", this.products[rand.nextInt(this.products.length)]);
    	json.put("price", rand.nextFloat());
    	LOG.debug("Generated:" + json.toString());

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode objNode = objectMapper.readTree(json.toString());

		ProducerRecord<String, JsonNode> record = new ProducerRecord<String, JsonNode>(
				this.topic, "event", objNode);
		this.producer.send(record);
	}
	
	public void start() throws Exception {
		Random rand = new Random();
		for (int i = 0; i < 1000; i++) {
			this.generate();
			Thread.sleep(rand.nextInt(150) + 50);
		}
	}
}
