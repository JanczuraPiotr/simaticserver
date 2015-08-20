package pjpl.simaticserver.process;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import pjpl.simaticserver.pdu.BramaDump;
import pjpl.simaticserver.run.SimaticServer;
import pjpl.simaticserver.util.FileJsonLogger;


/**
 * @author Piotr Janczura <piotr@janczura.pl>
 */
public class Brama implements Runnable{
	private DateFormat datePCFormat = new SimpleDateFormat(SimaticServer.config.getProperty("dateMSFormat"));
	private volatile String startTime = new String();
	private long msStart = 0;
	private long msStopRead = 0;
	private long msStop = 0;

	private pjpl.simaticserver.pdu.Brama Pdu;
	private pjpl.simaticserver.pdu.BramaDump PduDump;
	private pjpl.simaticserver.pdu.BramaAccess PduAccess;
	private pjpl.simaticserver.util.FileJsonLogger PduJsonLogger;
	private LinkedBlockingQueue<BramaDump> QueueDump;
	private Thread LoggerThread;

	public Brama(){
		Pdu = new pjpl.simaticserver.pdu.Brama();
		PduDump = Pdu.getBramaDump();
		PduAccess = Pdu.access();
		QueueDump = new LinkedBlockingQueue<>();
		PduJsonLogger = new FileJsonLogger(QueueDump, this);
		LoggerThread = new Thread(PduJsonLogger);
		LoggerThread.start();

	}
	public long getMsStartTime(){
		return msStart;
	}
	public String getStrStartTime(){
		return startTime;
	}
	protected void readPDU(){
		Pdu.readAll();
	}
	protected void writePDU(){

	}

	public void steep() throws InterruptedException{
		String outStr = new String();
		msStart = System.currentTimeMillis();
		startTime = datePCFormat.format(msStart);
		msStopRead = 0;
		msStop = 0;

		System.out.println("//------------------------------------------------------------------------------");
		System.out.println( datePCFormat.format(msStart)+" ProcessBrama.steep() Start");

		readPDU();

		msStopRead = System.currentTimeMillis();
		System.out.println( datePCFormat.format(msStopRead)+" ProcessBrama.steep() po odczycie PDU");

		PduDump = Pdu.getBramaDump();
		QueueDump.put(PduDump);

		msStop = System.currentTimeMillis();
		System.out.println( datePCFormat.format(msStop)+" ProcessBrama.steep() Stop praca = "+(msStop - msStart)+"[ms]");


	}

	public void run(){
		try {
			steep();
//		while(doSteep){
//			steep();
//		}
		} catch (InterruptedException ex) {
			Logger.getLogger(Brama.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

}
