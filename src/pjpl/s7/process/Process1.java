package pjpl.s7.process;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.naming.NamingException;
import pjpl.s7.common.CellCode;
import pjpl.s7.common.ConstPLC;
import pjpl.s7.run.SimaticServer;
import pjpl.s7.util.DumpThread;
import pjpl.s7.util.DumpExpertBinFile;
import pjpl.s7.util.DumpExpertBinMySql;


public class Process1 extends Process{

	public static final int id = Byte.parseByte( pjpl.s7.run.SimaticServer.config.getProperty("process_brama_id") );


	public Process1() throws NamingException, ClassNotFoundException, InstantiationException, IllegalAccessException{
		super();
		System.out.println("Process1 constructor");
		dumpFile = new DumpThread(
				memClip
				, new DumpExpertBinFile(
						SimaticServer.config.getProperty("dir_dump")
						, SimaticServer.config.getProperty("format_datePackedMS")
				)
		);

		dumpMySql = new DumpThread(
				memClip
				, new DumpExpertBinMySql(
						"jdbc:mysql://"+SimaticServer.config.getProperty("mysql_server")
								+":"+SimaticServer.config.getProperty("mysql_port")
								+"/"+SimaticServer.config.getProperty("mysql_db_name")
						,	SimaticServer.config.getProperty("mysql_user_name")
						,	SimaticServer.config.getProperty("mysql_user_password")
						, "process1"
				)
		);
	}

	@Override
	protected void steepRead(){
		super.steepRead();
		dumpFile.newData();
		dumpMySql.newData();
	}
	@Override
	protected void steep(){
		System.out.println("Process1.steepStart()");


		memD.write(CellCode.ZMIENNA_1, zmienna_1++);
		memD.write(CellCode.ZMIENNA_2, zmienna_2++);

		memQ.write(CellCode.OUT_1, out++);

		String s = "";
		byte[] mem;

		mem =	memD.getMem();
		for ( int i = 0 ; i < mem.length ; i++ ){
			s += " "+String.format("%02X", mem[i]);
		}
		System.out.println(" memD : " + s);
		s = "";
		mem =	memI.getMem();
		for( int i = 0; i < mem.length ; i++ ){
			s += " "+String.format("%02X", mem[i]);
		}
		System.out.println(" memI : "+ s);
		mem =	memQ.getMem();

		s = "";
		for( int i = 0; i < mem.length ; i++ ){
			s += " "+String.format("%02X", mem[i]);
		}
		System.out.println(" memQ : "+ s);


	}

	@Override
	protected void initMemory() {
		memD = new MemD(plcs);
		memI = new MemI(plcs);
		memQ = new MemQ(plcs);
	}

	@Override
	protected void initPlcs() {
		addPlc(
				ConstPLC.PLC1,
				new pjpl.s7.device.PLC1(
						SimaticServer.config.getProperty("plc_brama_ip")
						, Integer.parseInt( SimaticServer.config.getProperty("plc_brama_rack") )
						, Integer.parseInt( SimaticServer.config.getProperty("plc_brama_slot") )
				)
		);
	}

	@Override
	protected void steepException(Exception e) {
		System.err.println("Process1.steepException -> wyjątek : "+e.toString());
	}

	@Override
	protected void steepExceptionBis(Exception e) {
		System.out.println("Process1.steepExceptionBis -> wyjątek :  "+e.toString());
	}

	@Override
	public void steepExceptionFinally(Exception e) {
		System.err.println("Process1.steepExceptionFinally -> wyjątek : "+e.toString());
	}

	@Override
	public void steepFinaly() {
		System.out.println("Process1.steepFinally");
	}


	//------------------------------------------------------------------------------

	private short zmienna_1 = 3;
	private short zmienna_2 = 44;
	private static byte out = 1;

	private final DateFormat datePCFormat = new SimpleDateFormat(pjpl.s7.run.SimaticServer.config.getProperty("format_dateMS"));
	private volatile String startTime = new String();
	private long msProcess1Start = 0;
	private long msProcess1StopRead = 0;
	private long msProcess1Stop = 0;
	private String summaryRun;
	private DumpThread dumpFile;
	private DumpThread dumpMySql;

}
