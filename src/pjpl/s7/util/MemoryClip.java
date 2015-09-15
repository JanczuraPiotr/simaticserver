package pjpl.s7.util;

/**
 */
public class MemoryClip {
	public MemoryMap memoryD;
	public MemoryMap memoryI;
	public MemoryMap memoryQ;
	public long timeStamp;

	public MemoryClip(MemoryMap memoryD, MemoryMap memoryI, MemoryMap memoryQ, long timeStamp){
		this.memoryD = memoryD;
		this.memoryI = memoryI;
		this.memoryQ = memoryQ;
		this.timeStamp = timeStamp;

	}

	private MemoryClip(){}
}
