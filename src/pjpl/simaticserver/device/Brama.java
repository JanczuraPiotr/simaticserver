package pjpl.simaticserver.device;

import Moka7.S7;
import Moka7.S7Client;

/**
 * Sterownik S7 zlokalizowany na bramie
 * @author piotr
 */
public class Brama extends S7Client{
	public final int areaDBMaxLenght = 1024;
	public final int areaPAMaxLenght = 1024;
	public final int areaPEMaxLenght = 1024;

	private final int Rack = 0;
	private final int Slot = 0;
	private final String IP = "192.168.1.150";

	private int errCode = 0;

	private byte[] areaDB = new byte[areaDBMaxLenght];
	private int areaDBLenght;
	private byte[] areaPA = new byte[areaPAMaxLenght];
	private int areaPALenght;
	private byte[] areaPE = new byte[areaPEMaxLenght];
	private int areaPELenght;

	private BramaAccess access = null;

	public Brama(){
		super();
		ConnectTo();
	}
	public void ConnectTo(){
		SetConnectionType(S7.OP);
		errCode = super.ConnectTo(IP, Rack, Slot);
		System.err.println("Kod uruchonmienia sterownika na bramie " + errCode);

	}
	/**
	 * @return BramaDump
	 */
	public BramaDump getBramaDump(){
		return new BramaDump(
						areaDB, areaDBLenght,
						areaPA, areaPALenght,
						areaPE, areaPELenght);
	}
	/**
	 * @return
	 * @todo Zadbać by BramaInterface powstał tylko gdy isnieją dane w areaXX
	 */
	public BramaAccess access(){
		if( null == this.access ){
			access = new BramaAccess(
							areaDB, areaDBLenght,
							areaPA, areaPALenght,
							areaPE, areaPELenght);
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
