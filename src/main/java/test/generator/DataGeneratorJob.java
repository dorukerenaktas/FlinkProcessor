package test.generator;

public class DataGeneratorJob extends Thread {
	
	private SampleDataGenerator generator;
	public DataGeneratorJob(SampleDataGenerator generator) throws Exception {
		this.generator = generator;
	}
	
	public void run(){  
		try {
			this.generator.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}  
}
