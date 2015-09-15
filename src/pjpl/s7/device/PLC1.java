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

	private final int dbNumber = 1;
//	private int errCode = 0;

	private final byte[] areaDB = new byte[areaDBMaxLenght];
	private int areaDBLenght;
	private final byte[] areaPA = new byte[areaPAMaxLenght];
	private int areaPALenght;
	private final byte[] areaPE = new byte[areaPEMaxLenght];
	private int areaPELenght;
	private long timeStamp = 0; // Czas zakończenia odczytu
	private PlcAccess access = null;

	public PLC1(String IPAddress, int Rack, int Slot){
		super(IPAddress, Rack, Slot);
	}

	public PlcDump getBramaDump(){
		return new PlcDump(deviceName, areaDB, areaDBLenght,	areaPA, areaPALenght,	areaPE, areaPELenght,timeStamp);
	}
	/**
	 * @return
	 * @todo Zadbać by BramaInterface powstał tylko gdy istnieją dane w areaXX
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
		readAreaDb();
		readAreaIn();
		readAreaOut();
		timeStamp = System.currentTimeMillis();
	}
	@Override
	public void readAreaOut(){
		int status = ReadArea(S7.S7AreaPA, 0, 0, areaPAMaxLenght, areaPA);
		areaPALenght = PDULength();
	}
	@Override
	public void readAreaIn(){
		int status = ReadArea(S7.S7AreaPE, 0, 0, areaPEMaxLenght, areaPE);
		areaPELenght = PDULength();
	}
	@Override
	public void readAreaDb(){
		int status = ReadArea(S7.S7AreaDB, dbNumber, 0,6/* areaDBMaxLenght */, areaDB);
		areaDBLenght = PDULength();
	}
	public void writeAll(){
		writeAreaDb();
//		writeAreaPA();
	}
	@Override
	public void writeAreaDb(){
		byte[] out = { (byte)0xAA , (byte)0xBB }; //{99,2,3,4,5,6,7,8,9,0};
		int status = WriteArea(S7.S7AreaDB, dbNumber, 2, 2, out);
	}
	@Override
	public void writeAreaOut(){
		byte[] out = {9};
		int status = WriteArea(S7.S7AreaPA, dbNumber, 0, 1, out);
		System.out.println("status writeAreaPA = " + status);
	}

	@Override
	public int getSizeAreaD() {
		return 6;
	}

	@Override
	public int getSizeAreaI() {
		return 1;
	}

	@Override
	public int getSizeAreaQ() {
		return 1;
	}


}
