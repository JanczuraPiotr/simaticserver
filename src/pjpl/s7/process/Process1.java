package pjpl.s7.process;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.naming.NamingException;
import pjpl.s7.run.SimaticServer;
import pjpl.s7.util.Dump;
import pjpl.s7.util.DumpExpertBinFile;


public class Process1 extends Process{

	public static final int id = Byte.parseByte( pjpl.s7.run.SimaticServer.config.getProperty("process_brama_id") );
	public static final int PLC1 = 1;


	public Process1() throws NamingException, ClassNotFoundException, InstantiationException, IllegalAccessException{
		super();
		System.out.println("Process1 constructor");
		dump = new Dump(
				memoryClip
				, new DumpExpertBinFile(
						SimaticServer.config.getProperty("dir_dump")
						, SimaticServer.config.getProperty("format_dateMS")
				)
		);
	}

	@Override
	protected void steepRead(){
		dump.newData();
	}

	@Override
	protected void steep(){
		System.out.println("Process1.steepStart()");

		try {
			msProcess1Start = System.currentTimeMillis();

			startTime = datePCFormat.format(msProcess1Start);
			summaryRun = "------------------------------------------------------------------------------\n";
			summaryRun += datePCFormat.format(msProcess1Start) + " Process1.steep() Start \n";

			msProcess1Stop = System.currentTimeMillis();
			summaryRun += datePCFormat.format(msProcess1Stop) + " Process1.steep() Stop praca = " + (msProcess1Stop - msProcess1Start) + "[ms]\n";
		} finally {
			System.out.println(summaryRun);
			System.out.println("Process1.steepEnd()");
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
		addPlc(
				new pjpl.s7.device.PLC1(
						SimaticServer.config.getProperty("plc_brama_ip")
						, Integer.parseInt( SimaticServer.config.getProperty("plc_brama_rack") )
						, Integer.parseInt( SimaticServer.config.getProperty("plc_brama_slot") )
				)
		);
	}

	@Override
	public void steepException(Exception e) {
		System.err.println("Process1.steepException -> wyjątek : "+e.toString());
	}

	@Override
	public void steepExceptionFinally(Exception e) {
		System.err.println("Process1.steepExceptionFinally -> wyjątek : "+e.toString());
	}

	@Override
	public void steepFinaly() {
		System.err.println("Process1.steepFinally");
	}


	//------------------------------------------------------------------------------

	private final DateFormat datePCFormat = new SimpleDateFormat(pjpl.s7.run.SimaticServer.config.getProperty("format_dateMS"));
	private volatile String startTime = new String();
	private long msProcess1Start = 0;
	private long msProcess1StopRead = 0;
	private long msProcess1Stop = 0;
	private String summaryRun;
	private Dump dump;

}
