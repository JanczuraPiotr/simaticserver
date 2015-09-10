package pjpl.s7.device;

import Moka7.S7;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Stan pamięci procesora po odczycie z urządzenia fizycznego
 * @author Piotr Janczura <piotr@janczura.pl>
 */
public class PlcDump {
	private final int areaDBLenght;
	private final int areaPALenght;
	private final int areaPELenght;
	private final byte[] areaDB;
	private final byte[] areaPA;
	private final byte[] areaPE;
	private final long timeStamp; // Milisekunda w której odczytano stan urządzenia
	private final String deviceName;
	private PlcAccess access = null;

	public PlcDump(
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

		access = new PlcAccess(
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
	public String getDeviceName(){
		return deviceName;
	}
	public String hex(int areaCode){
		String hex = "";
		byte[] area;
		int areaLenght;

		switch(areaCode){
			case S7.S7AreaDB:
				area = areaDB;
				areaLenght = areaDBLenght;
				break;
			case S7.S7AreaPA:
				area = areaPA;
				areaLenght = areaPALenght;
					break;
			case S7.S7AreaPE:
				area = areaPE;
				areaLenght = areaPELenght;
				break;
			default:
				area = new byte[0];
				areaLenght = 0;
		}
		for(int i = 0 ; i < areaLenght; i++){
			hex += String.format("%02X", area[i]);
			hex += " ";
		}
		return hex;
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

}
