package pjpl.s7.device;

import Moka7.S7;

/**
 * Sterownik S7 
 */
public class PLC1 extends PLC{
	public final String deviceName = "brama";
	public final int areaDBMaxLenght = 1024;
	public final int areaPAMaxLenght = 1024;
	public final int areaPEMaxLenght = 1024;

	private final int Rack = Integer.parseInt( pjpl.s7.run.SimaticServer.config.getProperty("plc_brama_rack") );
	private final int Slot = Integer.parseInt( pjpl.s7.run.SimaticServer.config.getProperty("plc_brama_slot") );
	private final String IP = pjpl.s7.run.SimaticServer.config.getProperty("plc_brama_ip");
	private final int dbNumber = 1;
	private int errCode = 0;

	private final byte[] areaDB = new byte[areaDBMaxLenght];
	private int areaDBLenght;
	private final byte[] areaPA = new byte[areaPAMaxLenght];
	private int areaPALenght;
	private final byte[] areaPE = new byte[areaPEMaxLenght];
	private int areaPELenght;
	private long timeStamp = 0; // Czas zakończenia odczytu
	private PlcAccess access = null;

	public PLC1(){
		super();
		ConnectTo();
	}
	public void ConnectTo(){
		SetConnectionType(S7.OP);
		errCode = super.ConnectTo(IP, Rack, Slot);
		System.out.println("Kod uruchonmienia sterownika na bramie " + errCode);

	}
	public PlcDump getBramaDump(){
		return new PlcDump(deviceName, areaDB, areaDBLenght,	areaPA, areaPALenght,	areaPE, areaPELenght,timeStamp);
	}
	/**
	 * @return
	 * @todo Zadbać by BramaInterface powstał tylko gdy isnieją dane w areaXX
	 */
	public PlcAccess access(){
		if( null == this.access ){
			access = new PlcAccess(deviceName, areaDB, areaDBLenght, areaPA, areaPALenght,	areaPE, areaPELenght,timeStamp);
		}
		return access;
	}
	public long getTimeStamp(){
		return timeStamp;
	}

	public void readAll(){
		readAreaDB();
		readAreaPE();
		readAreaPA();
		timeStamp = System.currentTimeMillis();
	}
	private int readAreaPA(){
		int status = ReadArea(S7.S7AreaPA, 0, 0, areaPAMaxLenght, areaPA);
		areaPALenght = PDULength();
		return status;
	}
	private int readAreaPE(){
		int status = ReadArea(S7.S7AreaPE, 0, 0, areaPEMaxLenght, areaPE);
		areaPELenght = PDULength();
		return status;
	}
	private int readAreaDB(){
		int status = ReadArea(S7.S7AreaDB, dbNumber, 0,6/* areaDBMaxLenght */, areaDB);
		areaDBLenght = PDULength();
		return status;
	}

	public void writeAll(){
		writeAreaDB();
//		writeAreaPA();
	}

	private int writeAreaDB(){
		byte[] out = { (byte)0xAA , (byte)0xBB}; //{99,2,3,4,5,6,7,8,9,0};
		int status = WriteArea(S7.S7AreaDB, dbNumber, 2, 2, out);
		return status;
	}
	public int writeAreaPA(){
		byte[] out = {9};
		int status = WriteArea(S7.S7AreaPA, dbNumber, 0, 1, out);
		System.out.println("status writeAreaPA = " + status);
		return status;

	}


}
