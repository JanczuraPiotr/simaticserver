package pjpl.simaticserver.run;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Piotr Janczura <piotr@janczura.pl>
 */
public class SimaticServer {
	public static long time_interval;
	public static String configFile = "SimaticServer.ini";
	public static pjpl.simaticserver.util.Properties config;

	private static pjpl.simaticserver.process.Brama ProcessBrama;
	private static pjpl.simaticserver.util.DirCleaning dirCleaning;
	private static ScheduledExecutorService executor;
	private static FileReader configReader;
	private static FileWriter configWriter;

	public static void main(String[] args) throws IOException {

		try{
			configReader = new FileReader(configFile);
			config = new pjpl.simaticserver.util.Properties(new pjpl.simaticserver.run.ConfigDefault());
			config.load(configReader);
		}catch( FileNotFoundException e){
			configWriter = new FileWriter(configFile);
			config = new pjpl.simaticserver.util.Properties(new pjpl.simaticserver.run.ConfigDefault());
			config.store(configWriter,null);
		}
		time_interval = Long.parseLong(config.getProperty("time_interval"), 10);

		executor = Executors.newScheduledThreadPool(5);

		ProcessBrama = new pjpl.simaticserver.process.Brama();
		executor.scheduleAtFixedRate(ProcessBrama,  ( time_interval * 2 ) - ( System.currentTimeMillis() % time_interval) , time_interval, TimeUnit.MILLISECONDS);

		dirCleaning = new pjpl.simaticserver.util.DirCleaning(
				config.getProperty("dir_dump"),
				config.getProperty("format_fileName"),
				config.getProperty("time_storage")
		);
		executor.scheduleAtFixedRate(dirCleaning, 3000, Long.parseLong(config.getProperty("time_storageClean")), TimeUnit.MILLISECONDS);

	}

}
