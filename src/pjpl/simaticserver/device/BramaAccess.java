package pjpl.simaticserver.device;

import Moka7.S7;

/**
 * @author piotr
 */
public class BramaAccess extends S7{

	private final byte[] areaDB;
	private final int areaDBLenght;
	private final byte[] areaPA;
	private final int areaPALenght;
	private final byte[] areaPE;
	private final int areaPELenght;

	public BramaAccess(byte[] areaDB, int areaDBLenght,	byte[] areaPA, int areaPALenght, byte[] areaPE, int areaPELenght){
		this.areaDBLenght = areaDBLenght;
		this.areaPALenght = areaPALenght;
		this.areaPELenght = areaPELenght;
		this.areaDB = areaDB;
		this.areaPA = areaPA;
		this.areaPE = areaPE;
	}

	public byte[] getAreaDB(){
		return areaDB;
	};
	public byte[] getAreaPA(){
		return areaPA;
	};
	public byte[] getAreaPE(){
		return areaPE;
	};

	public int getAreaDBLenght(){
		return areaDBLenght;
	}
	public int getAreaPALenght(){
		return areaPALenght;
	}
	public int getAreaPELenght(){
		return areaPELenght;
	}

	public boolean input0(){
		return GetBitAt(areaPE, 0, 0);
	}
	public boolean input1(){
		return GetBitAt(areaPE, 0, 1);
	}
	public boolean input2(){
		return GetBitAt(areaPE, 0, 2);
	}
	public boolean input3(){
		return GetBitAt(areaPE, 0, 3);
	}
	public boolean input4(){
		return GetBitAt(areaPE, 0, 4);
	}
	public boolean input5(){
		return GetBitAt(areaPE, 0, 5);
	}
	public boolean input6(){
		return GetBitAt(areaPE, 0, 6);
	}
	public boolean input7(){
		return GetBitAt(areaPE, 0, 7);
	}

}
