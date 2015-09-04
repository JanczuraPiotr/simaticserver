package pjpl.s7.process;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;

/**
 * @author Piotr Janczura <piotr@janczura.pl>
 */
public class Process1 extends Process{
	protected final int id = Byte.parseByte( pjpl.s7.run.SimaticServer.config.getProperty("process_brama_id") );

	public Process1() throws NamingException, ClassNotFoundException, InstantiationException, IllegalAccessException{
		super();
		plcs = new HashMap<>();
		plc1 = new pjpl.s7.device.PLC1();
		plcs.put(id, plc1);

		DeviceDump = plc1.getBramaDump();
		DeviceAccess = plc1.access();
		QueueDump = new LinkedBlockingQueue<>();
		QueueMySql = new LinkedBlockingQueue<>();
		QueueCommand = new ConcurrentLinkedQueue<pjpl.s7.command.Command>();
		PduBinLogger = new pjpl.s7.util.FileBinLogger(QueueDump, this);
		LoggerThread = new Thread(PduBinLogger);
		LoggerThread.start();
		mySqlStore = new pjpl.s7.util.MySqlStore(QueueMySql, this);
		mySqlThread = new Thread(mySqlStore);
		mySqlThread.start();
	}
	public void addCommand(pjpl.s7.command.Command command){
		QueueCommand.add(command);
	}
	public long getMsStartTime(){
		return msStart;
	}
	public String getStrStartTime(){
		return startTime;
	}
	protected void readPDU(){
		plc1.readAll();
	}
	protected void process(){
		while( ! QueueCommand.isEmpty() ){
			System.out.println( QueueCommand.remove());
		}
	}
	public pjpl.s7.device.PLC getPlc(int nr){
		pjpl.s7.device.PLC plcReturn = null;
		switch(nr){
			case 1:
				plcReturn = plc1;
		}
		return plcReturn;
	}
	protected void writePDU(){
		plc1.writeAll();
	}

	public void steep() throws InterruptedException{
		msStart = System.currentTimeMillis();
		startTime = datePCFormat.format(msStart);
		msStopRead = 0;
		msStop = 0;

		summaryRun = "------------------------------------------------------------------------------\n";
		summaryRun += datePCFormat.format(msStart) + " Process1.steep() Start \n";

		readPDU();

		msStopRead = System.currentTimeMillis();
		summaryRun += datePCFormat.format(msStopRead) + " Process1.steep() po odczycie PDU \n";

		process();

		msStopRead = System.currentTimeMillis();
		summaryRun += datePCFormat.format(msStopRead) + " Process1.steep() wykonaniu przetwarzania \n";

		writePDU();

		msStopRead = System.currentTimeMillis();
		summaryRun += datePCFormat.format(msStopRead) + " Process1.steep() po zapisie PDU \n";

		DeviceDump = plc1.getBramaDump();
		QueueDump.put(DeviceDump);
		QueueMySql.put(DeviceDump);

		msStop = System.currentTimeMillis();
		summaryRun += datePCFormat.format(msStop) + " Process1.steep() Stop praca = " + (msStop - msStart) + "[ms]\n";


	}

	public void run(){
		try {
			steep();
		} catch (InterruptedException ex) {
			Logger.getLogger(Process1.class.getName()).log(Level.SEVERE, null, ex);
		} finally{
//			System.out.println(summaryRun);
		}
	}

	//------------------------------------------------------------------------------

	private final DateFormat datePCFormat = new SimpleDateFormat(pjpl.s7.run.SimaticServer.config.getProperty("format_dateMS"));
	private volatile String startTime = new String();
	private long msStart = 0;
	private long msStopRead = 0;
	private long msStop = 0;
	private String summaryRun;

 	private final pjpl.s7.device.PLC1 plc1;
	private final HashMap<Integer, pjpl.s7.device.PLC> plcs;
	private final pjpl.s7.device.BramaAccess DeviceAccess;
	private final pjpl.s7.util.FileBinLogger PduBinLogger;
	private final pjpl.s7.util.MySqlStore mySqlStore;
	private final LinkedBlockingQueue<pjpl.s7.device.BramaDump> QueueDump;
	private final LinkedBlockingQueue<pjpl.s7.device.BramaDump> QueueMySql;
	private final ConcurrentLinkedQueue<pjpl.s7.command.Command> QueueCommand;
	private final Thread LoggerThread;
	private final Thread mySqlThread;

	private pjpl.s7.device.BramaDump DeviceDump;

}
