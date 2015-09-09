package pjpl.s7.util;

import java.util.ArrayList;

/**
 * Mapuje zmienne procesowe na bloki danych sterowników obsługujących proces.
 */
abstract public class MemoryMap {

	abstract public void init();
	/**
	 * Zwraca pozycję w bloku pamięci na którym umieszczona będzie zmienna.
	 * @param size Rozmiar obecnie wstawianej zmiennej
	 * @return
	 */
	protected int pos(int size){
		int pos = currentPos;
		currentPos += size;
		return pos;
	}

	//------------------------------------------------------------------------------

	/**
	 * Aktualna pozycja pierwszego bajtu dla następnej zmiennej.
	 */
	private int currentPos;

	public ArrayList memoryMap;

	{
		currentPos = 0;
		memoryMap = new ArrayList();
	}


}
