package pjpl.simaticserver.device;

/**
 * Stan pamięci procesora po odczycie z urządzenia fizycznego
 * @author piotr
 */
public class BramaDump {
	private final int areaDBLenght;
	private final int areaPALenght;
	private final int areaPELenght;
	private final byte[] areaDB;
	private final byte[] areaPA;
	private final byte[] areaPE;
	private final byte[] timeStamp = new byte[100];
	private BramaAccess access = null;

	public BramaDump(byte[] areaDB, int areaDBLenght,	byte[] areaPA, int areaPALenght,	byte[] areaPE, int areaPELenght){
		this.areaDBLenght = areaDBLenght;
		this.areaPALenght = areaPALenght;
		this.areaPELenght = areaPELenght;

		this.areaDB = new byte[areaDBLenght];
		this.areaPA = new byte[areaPALenght];
		this.areaPE = new byte[areaPELenght];

		System.arraycopy(areaDB, 0, this.areaDB, 0, areaDBLenght);
		System.arraycopy(areaPA, 0, this.areaPA, 0, areaPALenght);
		System.arraycopy(areaPE, 0, this.areaPE, 0, areaPELenght);

		access = new BramaAccess(areaDB, areaDBLenght,areaPA, areaPALenght,	areaPE, areaPELenght);
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
