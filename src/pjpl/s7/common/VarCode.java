package pjpl.s7.common;

/**
 * Kody zmiennych procesowych
 * Dla każdego bloku danych indeksy muszą zaczynać się od 0
 */
abstract public class VarCode{
	// @todo Przenieść do tej klasy definicje dla mapy komórek w postaci tablicy a w MemMap.init() na podstawie tej tablicy budować mapę
	/**
	 * Byte
	 */
	public static final int ZMIENNA_BYTE = 0;
	/**
	 * Int
	 */
	public static final int ZMIENNA_INT = 1;
	/**
	 * DInt
	 */
	public static final int ZMIENNA_DINT = 2;
	/**
	 * Real
	 */
	public static final int ZMIENNA_REAL = 3;

	/**
	 * Byte
	 */
	public static final int IN_1  = 0;

	/**
	 * Byte
	 */
	public static final int OUT_1 = 0;
}
