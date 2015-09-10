package pjpl.s7.common;

/**
 * Kody zmiennych procesowych
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
 *
 * @todo opracować przypisywanie identyfikatorów do bitów
 */
abstract public class CellCode{

	public static final int ZMIENNA_1 = 0x0001;
	public static final int ZMIENNA_2 = 0x0002;
	public static final int ZMIENNA_3 = 0x0003;

	public static final int IN_1  = 0x1000;

	public static final int OUT_1 = 0x2000;
}
