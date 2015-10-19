package pjpl.s7.common;

import Moka7.S7;
import pjpl.s7.util.Bits;

/**
 *
 * @author pjanczura
 */
public class BitsCode {

	public static final int I_O_0 = prepare(S7.I, VarCode.IN_1, Bits.BIT_0);
	public static final int I_O_1 = prepare(S7.I, VarCode.IN_1, Bits.BIT_1);
	public static final int I_O_2 = prepare(S7.I, VarCode.IN_1, Bits.BIT_2);
	public static final int I_O_3 = prepare(S7.I, VarCode.IN_1, Bits.BIT_3);
	public static final int I_O_4 = prepare(S7.I, VarCode.IN_1, Bits.BIT_4);
	public static final int I_O_5 = prepare(S7.I, VarCode.IN_1, Bits.BIT_5);
	public static final int I_O_6 = prepare(S7.I, VarCode.IN_1, Bits.BIT_6);
	public static final int I_O_7 = prepare(S7.I, VarCode.IN_1, Bits.BIT_7);

	public static final int Q_0_0 = prepare(S7.Q, VarCode.OUT_1,Bits.BIT_0);
	public static final int Q_0_1 = prepare(S7.Q, VarCode.OUT_1,Bits.BIT_1);
	public static final int Q_0_2 = prepare(S7.Q, VarCode.OUT_1,Bits.BIT_2);
	public static final int Q_0_3 = prepare(S7.Q, VarCode.OUT_1,Bits.BIT_3);
	public static final int Q_0_4 = prepare(S7.Q, VarCode.OUT_1,Bits.BIT_4);
	public static final int Q_0_5 = prepare(S7.Q, VarCode.OUT_1,Bits.BIT_5);


	public static int prepare(byte typ, short varCode, byte bitNr){
		return  (
				( ( typ << 24 ) & 0xFF000000 ) +
				( ( varCode << 8 ) & 0x00FFF00 )+
				bitNr
		);
	}
	public static void parse(int bitCode){
		// @prace 00 zwróć obiekt opisiujący bit
	}
}
