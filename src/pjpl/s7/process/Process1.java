package pjpl.s7.process;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import pjpl.s7.run.SimaticServer;


public class Process1 extends Process{

	public static final int id = Byte.parseByte( pjpl.s7.run.SimaticServer.config.getProperty("process_brama_id") );
	public static final int PLC1 = 1;


	public Process1() throws NamingException, ClassNotFoundException, InstantiationException, IllegalAccessException{
		super();
		System.out.println("Process1 constructor");


		PlcDump = plc1.getBramaDump();
		DeviceAccess = plc1.access();
		QueueDump = new LinkedBlockingQueue<>();
		QueueMySql = new LinkedBlockingQueue<>();
		QueueCommand = new ConcurrentLinkedQueue<pjpl.s7.command.Command>();
//		PduBinLogger = new pjpl.s7.util.FileBinLogger(QueueDump, this);
//		LoggerThread = new Thread(PduBinLogger);
//		LoggerThread.start();
//		mySqlStore = new pjpl.s7.util.MySqlStore(QueueMySql, this);
//		mySqlThread = new Thread(mySqlStore);
//		mySqlThread.start();
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

	@Override
	public void steep(){
		System.out.println("Process1.steep()");
		try {
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

			PlcDump = plc1.getBramaDump();
			QueueDump.put(PlcDump);
//			QueueMySql.put(PlcDump);

			msStop = System.currentTimeMillis();
			summaryRun += datePCFormat.format(msStop) + " Process1.steep() Stop praca = " + (msStop - msStart) + "[ms]\n";
		} catch (InterruptedException ex) {
			Logger.getLogger(Process1.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			System.out.println(summaryRun);
		}


	}

	@Override
	protected void initMemory() {
		memoryD = new MemoryD(plcs);
		memoryI = new MemoryI(plcs);
		memoryQ = new MemoryQ(plcs);
	}

	@Override
	protected void initPlcs() {
		plc1 = new pjpl.s7.device.PLC1(
				SimaticServer.config.getProperty("plc_brama_ip")
				, Integer.parseInt( SimaticServer.config.getProperty("plc_brama_rack") )
				, Integer.parseInt( SimaticServer.config.getProperty("plc_brama_slot") )
		);
		addPlc(plc1);
	}

	@Override
	public void steepException(Exception e) {
		System.err.println("Process1.steepException -> wykątek : "+e.toString());
	}

	@Override
	public void steepExceptionFinally(Exception e) {
		System.err.println("Process1.steepExceptionFinally -> wykątek : "+e.toString());
	}

	@Override
	public void steepFinaly() {
		System.err.println("Process1.steepFinally");
	}


	//------------------------------------------------------------------------------

	private final DateFormat datePCFormat = new SimpleDateFormat(pjpl.s7.run.SimaticServer.config.getProperty("format_dateMS"));
	private volatile String startTime = new String();
	private long msStart = 0;
	private long msStopRead = 0;
	private long msStop = 0;
	private String summaryRun;

 	private pjpl.s7.device.PLC1 plc1;
	private final pjpl.s7.device.PlcAccess DeviceAccess;
//	private final pjpl.s7.util.FileBinLogger PduBinLogger;
//	private final pjpl.s7.util.MySqlStore mySqlStore;
	private final LinkedBlockingQueue<pjpl.s7.device.PlcDump> QueueDump;
	private final LinkedBlockingQueue<pjpl.s7.device.PlcDump> QueueMySql;
	private final ConcurrentLinkedQueue<pjpl.s7.command.Command> QueueCommand;
//	private final Thread LoggerThread;
//	private final Thread mySqlThread;

	private pjpl.s7.device.PlcDump PlcDump;
}
