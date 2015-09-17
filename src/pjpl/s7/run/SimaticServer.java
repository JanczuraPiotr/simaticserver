package pjpl.s7.run;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
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
	public static pjpl.s7.util.Properties config;
	public static long timeStart;

	private static HashMap<Integer, pjpl.s7.process.Process> processes;
	private static pjpl.s7.process.Process process1;

	private static pjpl.s7.util.DirCleaning dirCleaning;
	private static pjpl.s7.net.SocketListener socketListener;

	private static ScheduledExecutorService executor;
	private static FileReader configReader;
	private static FileWriter configWriter;

	public static void main(String[] args) throws IOException{
		timeStart = System.currentTimeMillis();
		System.out.println(SimaticServer.class.getName());

		try{
			configReader = new FileReader(configFile);
			config = new pjpl.s7.util.Properties(new pjpl.s7.run.ConfigDefault());
			config.load(configReader);
		}catch( FileNotFoundException e){
			try {
				configWriter = new FileWriter(configFile);
				config = new pjpl.s7.util.Properties(new pjpl.s7.run.ConfigDefault());
				config.store(configWriter,null);
			} catch (IOException ex) {
				Logger.getLogger(SimaticServer.class.getName()).log(Level.SEVERE, null, ex);
			}
		} catch (IOException ex) {
			Logger.getLogger(SimaticServer.class.getName()).log(Level.SEVERE, null, ex);
		}

		time_interval = Long.parseLong(config.getProperty("time_interval"), 10);
		processes = new HashMap<>();
		executor = Executors.newScheduledThreadPool(5);

		try {
			process1 = processes.put(
					Integer.getInteger(config.getProperty("process_brama_id"))
					, new pjpl.s7.process.Process1()
			);
			executor.scheduleAtFixedRate(
					processes.get(
							Integer.getInteger(
									config.getProperty("process_brama_id"))
					)
					, ( time_interval * 2 ) - ( System.currentTimeMillis() % time_interval)
					, time_interval
					, TimeUnit.MILLISECONDS
			);
			dirCleaning = new pjpl.s7.util.DirCleaning(
					config.getProperty("dir_dump")
					,	config.getProperty("format_fileName")
					,	config.getProperty("time_storage")
			);
			executor.scheduleAtFixedRate(
					dirCleaning, 3000, Long.parseLong(
							config.getProperty("time_storageClean")
					)
					, TimeUnit.MILLISECONDS
			);
			socketListener = new pjpl.s7.net.SocketListener();
			socketListener.start();

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
/**
 * @todo Wszystkie wystąpienia nazwy procesu : "Brama" zamienić na "process1"
 * @prace 10 utworzyć Command
 * @prace 20 modyfikować stan PLC za pomocą Command
 */