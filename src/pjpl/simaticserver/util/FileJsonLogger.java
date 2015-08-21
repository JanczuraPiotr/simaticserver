package pjpl.simaticserver.util;

import pjpl.simaticserver.device.BramaDump;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import pjpl.simaticserver.run.SimaticServer;

/**
 * Zapisywanie stanu procesora do pliku textowego.
 * Kompletny odczyt stanu sterownika zapisany jest jako jeden obiekt JSON. Nazwą obiektu jest timestamp zebrania danych.
 * @author Piotr Janczura <piotr@janczura.pl>
 */
public class FileJsonLogger implements Runnable{
	private final String dirDump = SimaticServer.config.getProperty("dirDump");
	private final DateFormat dateFormat = new SimpleDateFormat(SimaticServer.config.getProperty("dateMSFormat"));
	private final DateFormat dateFileNameFormat = new SimpleDateFormat(SimaticServer.config.getProperty("dateMSPackedFormat"));
	private final LinkedBlockingQueue<BramaDump> queue;
	private volatile long timeStart;
	private volatile long timeStop;
	private final String s = new String();
	private String json = new String();
	private final pjpl.simaticserver.process.Brama parent;

	public FileJsonLogger(LinkedBlockingQueue<BramaDump> queue, pjpl.simaticserver.process.Brama parent){
		this.queue = queue;
		this.parent = parent;
		System.out.println(SimaticServer.timeInterval);
	}

	@Override
	public void run() {
		while(true){
			try {

				System.out.println( dateFormat.format(System.currentTimeMillis())+" FileJsonLogger.run() Start czyli czakanie na kolejkę");
				json = queue.take().json();

				System.out.println( dateFormat.format( timeStart = System.currentTimeMillis() )+" FileJsonLogger.run() Po queue.take().json()");
				FileWriter writer = new FileWriter( dirDump + dateFileNameFormat.format(parent.getMsStartTime())+".pdu");
				writer.write(json);
				writer.close();

				System.out.println( dateFormat.format(System.currentTimeMillis())+" FileJsonLogger.run() utworzono plik : "+parent.getMsStartTime()+".pdu");
				json = "\""+ parent.getMsStartTime() + "\" : "+json+"";

				System.out.println( dateFormat.format( timeStop = System.currentTimeMillis() )+" FileJsonLogger.run() Stop praca = "+ (timeStop-timeStart)+"[ms]");
				System.out.print(s);

			} catch (InterruptedException ex) {
				Logger.getLogger(FileJsonLogger.class.getName()).log(Level.SEVERE, null, ex);
			} catch (IOException ex) {
				Logger.getLogger(FileJsonLogger.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

}
