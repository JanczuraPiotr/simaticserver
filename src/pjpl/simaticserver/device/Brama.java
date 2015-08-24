package pjpl.simaticserver.device;

import Moka7.S7;
import Moka7.S7Client;
import pjpl.simaticserver.run.SimaticServer;

/**
 * Sterownik S7 zlokalizowany na bramie
 * @author piotr
 */
public class Brama extends S7Client{
	public final int areaDBMaxLenght = 1024;
	public final int areaPAMaxLenght = 1024;
	public final int areaPEMaxLenght = 1024;

	private final int Rack = Integer.parseInt( SimaticServer.config.getProperty("plc_brama_rack") );
	private final int Slot = Integer.parseInt( SimaticServer.config.getProperty("plc_brama_slot") );
	private final String IP = SimaticServer.config.getProperty("plc_brama_ip");

	private int errCode = 0;

	private final byte[] areaDB = new byte[areaDBMaxLenght];
	private int areaDBLenght;
	private final byte[] areaPA = new byte[areaPAMaxLenght];
	private int areaPALenght;
	private final byte[] areaPE = new byte[areaPEMaxLenght];
	private int areaPELenght;

	private BramaAccess access = null;

	public Brama(){
		super();
		ConnectTo();
	}
	public void ConnectTo(){
		SetConnectionType(S7.OP);
		errCode = super.ConnectTo(IP, Rack, Slot);
		System.out.println("Kod uruchonmienia sterownika na bramie " + errCode);

	}
	public BramaDump getBramaDump(){
		return new BramaDump(areaDB, areaDBLenght,	areaPA, areaPALenght,	areaPE, areaPELenght);
	}
	/**
	 * @todo Zadbać by BramaInterface powstał tylko gdy isnieją dane w areaXX
	 */
	public BramaAccess access(){
		if( null == this.access ){
			access = new BramaAccess(areaDB, areaDBLenght, areaPA, areaPALenght,	areaPE, areaPELenght);
		}
		return access;
	}


	public void readAll(){
		readAreaDB();
		readAreaPE();
		readAreaPA();
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
		int status = ReadArea(S7.S7AreaDB, 0, 0, areaDBMaxLenght, areaDB);
		areaDBLenght = PDULength();
		return status;
	}

	public void writeAll(){

	}


}
