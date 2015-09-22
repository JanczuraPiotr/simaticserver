package pjpl.s7.common;

/**
 * Kody sterujące pracą programu
 */
abstract public class CommandCode{
	private static int codeInc = 0;
	
	// Kody nie ujmowane w przedziały
	public static final int OK = 0x0000;
	public static final int NO = 0xFFFF;

	// Stałe ogólne
	// 000000000000000B -> 0000111111111111B => 0x0000 -> 0x0FFF => 0 ->  4095

	// Stałe wewnętrzne biblioteki pjpl.s7
	// 000100000000000B -> 0001111111111111B => 0x1000 -> 0x1FFF => 4096 ->  8191

	// nieprzydzielone:
	// 001000000000000B -> 0011111111111111B => 0x2000 -> 0x3FFF =>   8192 -> 16383


	// Kody od 0x8000 wydzielone są dla klas definiujących algorytmy do wykonania w ramach procesu do którego są skierowane.
	// Kod może wymagać parametrów do pracy i te umieszczone są w tym samym buforze zaraz za nim. Obsługą kody wraz z
	// odczytaniem parametrów i wykonaniem algorytmu w ramach procesu zajmują się obiekty klas pochodnych po Command.

	// Kody komend niezależnych od procesu.
	// 100000000000000B -> 1011111111111111B => 0x8000 -> 0xBFFF =>  32768 -> 49151

	public static final int GET_BIT_IN  = 0x8001;
	public static final int GET_BIT_OUT = 0x8002;
	public static final int GET_BIT     = 0x8003;
	public static final int GET_BYTE    = 0x8004;
	public static final int GET_WORD    = 0x8005;

	public static final int SET_BIT_OUT = 0x8006;
	public static final int SET_BIT     = 0x8007;
	public static final int SET_BYTE    = 0x8008;
	public static final int SET_WORD    = 0x8009;

	// Kody komend indywidualne dla tej aplikacji
	// 110000000000000B -> 1111111111111111B => 0xC000 -> 0xCFFF =>  49152 -> 65535

}
