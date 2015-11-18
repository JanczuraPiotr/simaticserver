package pjpl.s7.common;

/**
 * Typy zmiennych występujących w sterowniku PLC.S7
 */
public class TypeCode {

	public static final int PORT   = 0x0000000000;
	public static final int BYTE  = 0x0000000001; // 8 bit
	public static final int INT   = 0x0000000002; // 16 bit
	public static final int DINT  = 0x0000000003; // 32 bit
	public static final int REAL  = 0x0000000004; // 32 bit float
	public static final int LREAL = 0x0000000005; // 64 bit double



	public static final int[] occupiedMemSpace = new int[6];
	static{
		occupiedMemSpace[PORT]  = 1;
		occupiedMemSpace[BYTE]  = 2;
		occupiedMemSpace[INT]   = 2;
		occupiedMemSpace[DINT]  = 4;
		occupiedMemSpace[REAL]  = 4;
		occupiedMemSpace[LREAL] = 8;
	}
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
		name[PORT]  = "Port";
		name[BYTE]  = "Byte";
		name[INT]   = "Int";
		name[DINT]  = "DInt";
		name[REAL]  = "Real";
		name[LREAL] = "LReal";
	}
}
/* Próba zapisania inforamcji o typie kodu w stałej kodowej
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