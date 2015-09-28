package pjpl.s7.util;

/**
 * Spinacz na wszystkie mapy pamięci występujące w jednym urządzeniu zebrane w jednej chwili.
 */
public class MemClip {
	public MemMap memD;
	public MemMap memI;
	public MemMap memQ;
	/**
	 * Moment wykonania zrzutu.
	 * Jeżeli < 0 to dane utworzono wewnątrz SimaticServer
	 */
	public long timeStamp;
	/**
	 * PLC w którym wykonano zrzut.
	 * Jeżeli < 0 to dane powstały wewnątrz SimaticServer
	 */
	public int plcId;

	public MemClip(MemMap memD, MemMap memI, MemMap memQ, long timeStamp, int plcId){
		this.memD = memD;
		this.memI = memI;
		this.memQ = memQ;
		this.timeStamp = timeStamp;
		this.plcId = plcId;
	}

	public MemClip(MemMap memD, MemMap memI, MemMap memQ){
		this.memD = memD;
		this.memI = memI;
		this.memQ = memQ;
		this.timeStamp = -1;
		this.plcId = -1;
	}

	//------------------------------------------------------------------------------
	// interfejs

	public MemByteClip toMemByteClip(){
		return new MemByteClip(memD.getMemCopy(), memI.getMemCopy(), memQ.getMemCopy(), timeStamp, plcId);
	}

	// interfejs
	//------------------------------------------------------------------------------

	//------------------------------------------------------------------------------
	// metody prywatne


private MemClip(){}
}
