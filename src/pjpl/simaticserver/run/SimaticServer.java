package pjpl.simaticserver.run;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * @author piotr
 */
public class SimaticServer {
	public static long timeInterval;
	public static String configFile = "SimaticServer.ini";
	public static Properties config;

	private static pjpl.simaticserver.process.Brama ProcessBrama;
	private static ScheduledExecutorService executor;
	private static FileReader configReader;
	private static FileWriter configWriter;

	public static void main(String[] args) throws IOException {

		try{
			configReader = new FileReader(configFile);
			config = new Properties(new pjpl.simaticserver.run.ConfigDefault());
			config.load(configReader);
		}catch( FileNotFoundException e){
			configWriter = new FileWriter(configFile);
			config = new Properties(new pjpl.simaticserver.run.ConfigDefault());
			config.store(configWriter,null);
		}
		timeInterval = Long.parseLong(config.getProperty("timeInterval"), 10);

		executor = Executors.newScheduledThreadPool(5);
		ProcessBrama = new pjpl.simaticserver.process.Brama();
		executor.scheduleAtFixedRate(ProcessBrama,  ( timeInterval * 2 ) - ( System.currentTimeMillis() % timeInterval) , timeInterval, TimeUnit.MILLISECONDS);

	}

}
