package pjpl.s7.util;

/**
 */
public class MemoryByteClip{
	public byte[] buffD;
	public byte[] buffI;
	public byte[] buffQ;
	public long timeStamp;

	public MemoryByteClip(byte[] buffD, byte[] buffI, byte[] buffQ, long timeStamp){
		this.buffD = buffD;
		this.buffI = buffI;
		this.buffQ = buffQ;
		this.timeStamp = timeStamp;
	}

	private MemoryByteClip(){}
}
