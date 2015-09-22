package pjpl.s7.common;

/**
 * Kody zmiennych procesowych
 * Dla każdego bloku danych indexy muszą zaczynać się od 0
 */
abstract public class VarCode{
	// @todo Przenieść do tej klasy definicje dla mapy komórek w postaci tablicy a w MemMap.init() na podstawie tej tablicy budowac mapę
	/**
	 * DInt
	 */
	public static final int ZMIENNA_1 = 0;
	/**
	 * DInt
	 */
	public static final int ZMIENNA_2 = 1;
	/**
	 * DInt
	 */
	public static final int ZMIENNA_3 = 2;

	/**
	 * Byte
	 */
	public static final int IN_1  = 0;

	/**
	 * Byte
	 */
	public static final int OUT_1 = 0;
}
