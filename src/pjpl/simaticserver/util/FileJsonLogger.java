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

	public FileJsonLogger(LinkedBlockingQueue<BramaDump> queue, pjpl.simaticserver.process.Brama parent){
		this.queue = queue;
		this.parent = parent;
	}

	@Override
	public void run() {
		while(true){
			try {
				summaryRun = "------------------------------------------------------------------------------\n";
				summaryRun += format_date.format(System.currentTimeMillis()) + " FileJsonLogger.run() Start czyli czakanie na kolejkę\n";
				json = queue.take().json();

				summaryRun += format_date.format( timeStart = System.currentTimeMillis() )+" FileJsonLogger.run() Po queue.take().json()\n";
				FileWriter writer = new FileWriter( dir_dump + "/" + dateFileNameFormat.format(parent.getMsStartTime())+".pdu");
				writer.write(json);
				writer.close();

				summaryRun += format_date.format(System.currentTimeMillis())+" FileJsonLogger.run() utworzono plik : "+dateFileNameFormat.format(parent.getMsStartTime())+".pdu\n";
				summaryRun += format_date.format(parent.getMsStartTime()) + "\n"+json+"\n";
				summaryRun += format_date.format( timeStop = System.currentTimeMillis() )+" FileJsonLogger.run() Stop praca = "+ (timeStop-timeStart)+"[ms]\n";

			} catch (InterruptedException ex) {
				Logger.getLogger(FileJsonLogger.class.getName()).log(Level.SEVERE, null, ex);
			} catch (IOException ex) {
				Logger.getLogger(FileJsonLogger.class.getName()).log(Level.SEVERE, null, ex);
			} finally {
				System.out.println(summaryRun);

			}
				System.out.println(summaryRun);
		}
	}

	//------------------------------------------------------------------------------
	private final String dir_dump = SimaticServer.config.getProperty("dir_dump");
	private final DateFormat format_date = new SimpleDateFormat(SimaticServer.config.getProperty("format_dateMS"));
	private final DateFormat dateFileNameFormat = new SimpleDateFormat(SimaticServer.config.getProperty("format_datePackedMS"));
	private final LinkedBlockingQueue<BramaDump> queue;
	private volatile long timeStart;
	private volatile long timeStop;
	private String summaryRun = new String();
	private String json = new String();
	private final pjpl.simaticserver.process.Brama parent;

}
