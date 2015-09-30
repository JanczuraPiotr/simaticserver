package pjpl.s7.run;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import pjpl.s7.common.ConstProcess;

/**
 * @author Piotr Janczura <piotr@janczura.pl>
 */
public class SimaticServer {
	public static String configFile = "SimaticServer.ini";
	public static pjpl.s7.util.Properties config;
	public static long time_interval;
	public static long timeStart;

	public static void main(String[] args) throws IOException{
		timeStart = System.currentTimeMillis();
		System.out.println(SimaticServer.class.getName());

		//------------------------------------------------------------------------------
		// konfiguracja

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

		// konfiguracja
		//------------------------------------------------------------------------------

		//------------------------------------------------------------------------------
		// inicjacja zmiennych

		time_interval = Long.parseLong(config.getProperty("time_interval"), 10);
		processes = new HashMap<>();
		executor = Executors.newScheduledThreadPool(5);

		// inicjacja zmiennych
		//------------------------------------------------------------------------------

		try {

			//------------------------------------------------------------------------------
			// procesy

			process1 = new pjpl.s7.process.Process1(ConstProcess.PROCESS1_ID);

			processes.put(process1.id(), process1 );

			// procesy
			//------------------------------------------------------------------------------

			//------------------------------------------------------------------------------
			// komunikacja

			commandWebListener = new pjpl.s7.command.CommandWebListener(Integer.parseInt(config.getProperty("web-listener-port")));
			commandWebListener.addQueue(process1.id(), process1.getCommadQueue());
			commandWebListener.start();

			// komunikacja
			//------------------------------------------------------------------------------

			//------------------------------------------------------------------------------
			// pomocnicze

			dirCleaning = new pjpl.s7.util.DirCleaning(
					config.getProperty("dir_dump")
					,	config.getProperty("format_fileName")
					,	config.getProperty("time_storage")
			);

			// pomocnicze
			//------------------------------------------------------------------------------

			//------------------------------------------------------------------------------
			// uruchamianie

			executor.scheduleAtFixedRate(
					processes.get(process1.id())
					, ( time_interval * 2 ) - ( System.currentTimeMillis() % time_interval)
					, time_interval
					, TimeUnit.MILLISECONDS
			);
			executor.scheduleAtFixedRate(
					dirCleaning, 3000, Long.parseLong(
							config.getProperty("time_storageClean")
					)
					, TimeUnit.MILLISECONDS
			);

			// uruchamianie
			//------------------------------------------------------------------------------

		} catch (NullPointerException | IOException | NamingException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
			Logger.getLogger(SimaticServer.class.getName()).log(Level.SEVERE, null, ex);
		}

	}


	private static HashMap<Byte, pjpl.s7.process.Process> processes;
	private static pjpl.s7.process.Process process1;
	private static Queue<pjpl.s7.command.Command> commandQueue;

	private static pjpl.s7.util.DirCleaning dirCleaning;
	private static pjpl.s7.command.CommandWebListener commandWebListener;

	private static ScheduledExecutorService executor;
	private static FileReader configReader;
	private static FileWriter configWriter;

}
/**
 * @prace 30 modyfikować stan PLC za pomocą Command
 *
 * @todo Wszystkie wystąpienia nazwy procesu : "Brama" zamienić na "process1"
 * @todo Kolejki komend powinne pozwolić przekazywać komendy pomiędzy procesami.
 *				Prawdopodobnie każdy Process będzie zwracał swoją kolejkę która będzie przekazana do CommandListenera
 */