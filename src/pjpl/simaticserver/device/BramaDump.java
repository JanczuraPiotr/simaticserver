package pjpl.simaticserver.device;

import Moka7.S7;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Array;

/**
 * Stan pamięci procesora po odczycie z urządzenia fizycznego
 * @author Piotr Janczura <piotr@janczura.pl>
 */
public class BramaDump {
	private final int areaDBLenght;
	private final int areaPALenght;
	private final int areaPELenght;
	private final byte[] areaDB;
	private final byte[] areaPA;
	private final byte[] areaPE;
	private final long timeStamp; // Milisekunda w której odczytano stan urządzenia
	private String deviceName;
	private BramaAccess access = null;

	public BramaDump(
			String deviceName,
			byte[] areaDB,
			int areaDBLenght,
			byte[] areaPA,
			int areaPALenght,
			byte[] areaPE,
			int areaPELenght,
			long timeStamp){

		this.deviceName = deviceName;
		this.areaDBLenght = areaDBLenght;
		this.areaPALenght = areaPALenght;
		this.areaPELenght = areaPELenght;
		this.timeStamp = timeStamp;

		this.areaDB = new byte[areaDBLenght];
		this.areaPA = new byte[areaPALenght];
		this.areaPE = new byte[areaPELenght];

		System.arraycopy(areaDB, 0, this.areaDB, 0, areaDBLenght);
		System.arraycopy(areaPA, 0, this.areaPA, 0, areaPALenght);
		System.arraycopy(areaPE, 0, this.areaPE, 0, areaPELenght);

		access = new BramaAccess(
				deviceName,
				areaDB,
				areaDBLenght,
				areaPA,
				areaPALenght,
				areaPE,
				areaPELenght,
				timeStamp);
	}
	public long getTimeStamp(){
		return timeStamp;
	}
	public String getDivaceName(){
		return deviceName;
	}

	public InputStream byteInput(int areaCode){
		InputStream inStream = null;

		switch(areaCode){
			case S7.S7AreaDB:
				inStream = new ByteArrayInputStream(areaDB);
				break;
			case S7.S7AreaPA:
				inStream = new ByteArrayInputStream(areaPA);
				break;
			case S7.S7AreaPE:
				inStream = new ByteArrayInputStream(areaPE);
				break;
		}

		return inStream;
	}
	public ByteArrayOutputStream bin(int areaCode){
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();

		switch(areaCode){
			case S7.S7AreaDB:
				outStream.write(areaDB, 0, areaDBLenght);
				break;
			case S7.S7AreaPA:
				outStream.write(areaPA, 0, areaPALenght);
				break;
			case S7.S7AreaPE:
				outStream.write(areaPE, 0, areaPELenght);
				break;
		}

		return outStream;
	}
	public String json(){
		String json
					= "{"
					+	"Input0 : " + access.input0() + ","
					+	"Input1 : " + access.input1() + ","
					+	"Input2 : " + access.input2() + ","
					+	"Input3 : " + access.input3() + ","
					+	"Input4 : " + access.input4() + ","
					+	"Input5 : " + access.input5() + ","
					+	"Input6 : " + access.input6() + ","
					+	"Input7 : " + access.input7() + ""
					+ "}";
		return json;
	}

	public InputStream byteInput() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}
