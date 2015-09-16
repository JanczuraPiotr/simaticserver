package pjpl.s7.common;

/**
 * Typy zmiennych występujących w sterowniku PLC.S7
 */
public class TypeCode {

	public static final int BIT   = 0;
	public static final int BYTE  = 1; // 8 bit
	public static final int INT   = 2; // 16 bit
	public static final int DINT  = 3; // 32 bit
	public static final int REAL  = 4; // 32 bit float
	public static final int LREAL = 5; // 64 bit double



	public static final int[] size = new int[6];
	static{
		size[BYTE]  = 1;
		size[INT]   = 2;
		size[DINT]  = 4;
		size[REAL]  = 4;
		size[LREAL] = 8;
	}

	public static final String[] name = new String[6];
	static{
		name[BYTE]  = "Byte";
		name[INT]   = "Int";
		name[DINT]  = "DInt";
		name[REAL]  = "Real";
		name[LREAL] = "LReal";
	}
}
/*
 *
 * Podział kodów.
 * 0x0000
 *   ||||
 *   ||  dwa mniej znaczące bajty kodują zmienną
 *   | trzeci bajt koduje typ zmiennej
 *    czwarty bajt koduje obszar pamięci.
 *
 * Znaczenia bajtów:
 * bajt nr 0
 * bajt nr 1
 *		kod zmiennej
 *
 * bajt nr 2
 *		0x0 bit
 *		0x1 bajt
 *		0x2 ...
 * bajt nr 3
 *		0x0 db
 *		0x1 in
 *		0x2 out
 *		0x3 ...
 *
 * 0x0000 db jako bajt
 * 0x1000 in jako bajt
 * 0x2000 out jako bajt
 */