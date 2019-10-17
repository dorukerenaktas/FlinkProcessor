package test.processor;

import org.apache.log4j.BasicConfigurator;

import test.generator.DataGeneratorJob;
import test.generator.SampleDataGenerator;
import test.processor.cep.FlinkCEProcessor;
import test.processor.pattern.FlinkPatternProvider;
import test.processor.rule.LocalRuleProvider;

public class Processor {
	public static void main(String[] args) throws Exception {
		BasicConfigurator.configure();
		
		SampleDataGenerator generator = new SampleDataGenerator("localhost:9092", "Events");
		DataGeneratorJob job = new DataGeneratorJob(generator);
		job.start();
		
		LocalRuleProvider ruleProvider = new LocalRuleProvider();
		FlinkPatternProvider patternProvider = new FlinkPatternProvider(ruleProvider);

		FlinkCEProcessor processor = new FlinkCEProcessor(patternProvider, "localhost:9092", "Events", "Actions");
		processor.process();
	}
}
