package pjpl.simaticserver.process;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import pjpl.simaticserver.device.BramaDump;
import pjpl.simaticserver.run.SimaticServer;


/**
 * @author Piotr Janczura <piotr@janczura.pl>
 */
public class Brama implements Runnable{
	private final DateFormat datePCFormat = new SimpleDateFormat(SimaticServer.config.getProperty("format_dateMS"));
	private volatile String startTime = new String();
	private long msStart = 0;
	private long msStopRead = 0;
	private long msStop = 0;
	private String summaryRun;

 	private final pjpl.simaticserver.device.Brama Device;
	private pjpl.simaticserver.device.BramaDump DeviceDump;
	private final pjpl.simaticserver.device.BramaAccess DeviceAccess;
	private final pjpl.simaticserver.util.FileBinLogger PduBinLogger;
	private final pjpl.simaticserver.util.MySqlStore mySqlStore;
	private final LinkedBlockingQueue<BramaDump> QueueDump;
	private final LinkedBlockingQueue<BramaDump> QueueMySql;
	private final Thread LoggerThread;
//	private final Thread mySqlThread;

	public Brama() throws NamingException, ClassNotFoundException, InstantiationException, IllegalAccessException{
		Device = new pjpl.simaticserver.device.Brama();
		DeviceDump = Device.getBramaDump();
		DeviceAccess = Device.access();
		QueueDump = new LinkedBlockingQueue<>();
		QueueMySql = new LinkedBlockingQueue<>();
		PduBinLogger = new pjpl.simaticserver.util.FileBinLogger(QueueDump, this);
		LoggerThread = new Thread(PduBinLogger);
		LoggerThread.start();
		mySqlStore = new pjpl.simaticserver.util.MySqlStore(QueueMySql, this);
//		mySqlThread = new Thread(mySqlStore);
//		mySqlThread.start();

	}
	public long getMsStartTime(){
		return msStart;
	}
	public String getStrStartTime(){
		return startTime;
	}
	protected void readPDU(){
		Device.readAll();
	}
	protected void writePDU(){

	}

	public void steep() throws InterruptedException{
		msStart = System.currentTimeMillis();
		startTime = datePCFormat.format(msStart);
		msStopRead = 0;
		msStop = 0;

		summaryRun = "------------------------------------------------------------------------------\n";
		summaryRun += datePCFormat.format(msStart) + " ProcessBrama.steep() Start \n";

		readPDU();

		msStopRead = System.currentTimeMillis();
		summaryRun += datePCFormat.format(msStopRead) + " ProcessBrama.steep() po odczycie PDU \n";

		DeviceDump = Device.getBramaDump();
		QueueDump.put(DeviceDump);
		QueueMySql.put(DeviceDump);

		msStop = System.currentTimeMillis();
		summaryRun += datePCFormat.format(msStop) + " ProcessBrama.steep() Stop praca = " + (msStop - msStart) + "[ms]\n";


	}

	public void run(){
		try {
			steep();
		} catch (InterruptedException ex) {
			Logger.getLogger(Brama.class.getName()).log(Level.SEVERE, null, ex);
		} finally{
			System.out.println(summaryRun);
		}
	}

}
