package pjpl.simaticserver.run;

import com.mysql.jdbc.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;

/**
 * @author Piotr Janczura <piotr@janczura.pl>
 */
public class SimaticServer {
	public static long time_interval;
	public static String configFile = "SimaticServer.ini";
	public static pjpl.simaticserver.util.Properties config;
	public static long timeStart;

	private static pjpl.simaticserver.process.Brama ProcessBrama;
	private static pjpl.simaticserver.util.DirCleaning dirCleaning;
	private static ScheduledExecutorService executor;
	private static FileReader configReader;
	private static FileWriter configWriter;

	public static void main(String[] args){
		timeStart = System.currentTimeMillis();

		try{
			configReader = new FileReader(configFile);
			config = new pjpl.simaticserver.util.Properties(new pjpl.simaticserver.run.ConfigDefault());
			config.load(configReader);
		}catch( FileNotFoundException e){
			try {
				configWriter = new FileWriter(configFile);
				config = new pjpl.simaticserver.util.Properties(new pjpl.simaticserver.run.ConfigDefault());
				config.store(configWriter,null);
			} catch (IOException ex) {
				Logger.getLogger(SimaticServer.class.getName()).log(Level.SEVERE, null, ex);
			}
		} catch (IOException ex) {
			Logger.getLogger(SimaticServer.class.getName()).log(Level.SEVERE, null, ex);
		}

		time_interval = Long.parseLong(config.getProperty("time_interval"), 10);

		executor = Executors.newScheduledThreadPool(5);

		try {
			ProcessBrama = new pjpl.simaticserver.process.Brama();

			executor.scheduleAtFixedRate(ProcessBrama,  ( time_interval * 2 ) - ( System.currentTimeMillis() % time_interval) , time_interval, TimeUnit.MILLISECONDS);

			dirCleaning = new pjpl.simaticserver.util.DirCleaning(
					config.getProperty("dir_dump"),
					config.getProperty("format_fileName"),
					config.getProperty("time_storage")
			);
			executor.scheduleAtFixedRate(dirCleaning, 3000, Long.parseLong(config.getProperty("time_storageClean")), TimeUnit.MILLISECONDS);

		} catch (NamingException ex) {
			Logger.getLogger(SimaticServer.class.getName()).log(Level.SEVERE, null, ex);
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(SimaticServer.class.getName()).log(Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			Logger.getLogger(SimaticServer.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			Logger.getLogger(SimaticServer.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

}
