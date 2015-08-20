package pjpl.simaticserver.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import pjpl.simaticserver.pdu.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import pjpl.simaticserver.run.SimaticServer;

/**
 * Zapisywanie stanu procesora do pliku textowego.
 * Kompletny odczyt stanu PDU zapisany jest jako jeden obiekt JSON. Nazwą obiektu jest timestamp zebrania danych.
 * @author Piotr Janczura <piotr@janczura.pl>
 */
public class FileJsonLogger implements Runnable{
	private DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss:SSS");
	private DateFormat dateFileNameFormat = new SimpleDateFormat(SimaticServer.config.getProperty("datePackedFormat"));
	private LinkedBlockingQueue<BramaDump> queue;
	private volatile long timeStart;
	private volatile long timeStop;
	private String s = new String();
	private String json = new String();
	private pjpl.simaticserver.process.Brama parent;

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
				FileWriter writer = new FileWriter( dateFileNameFormat.format(parent.getMsStartTime())+".pdu");
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
