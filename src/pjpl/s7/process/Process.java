package pjpl.s7.process;

import java.util.HashMap;
import pjpl.s7.util.MemoryMap;

/**
 * @author Piotr Janczura <piotr@janczura.pl>
 */
abstract public class Process implements Runnable{

	protected MemoryMap memoryDB = new MemoryDb();
	protected MemoryIn memoryIn = new MemoryIn();
	protected MemoryOut memouryOut = new MemoryOut();
	// @todo opracować pozostałe bloki pamięci.
	protected HashMap<Integer, pjpl.s7.device.PLC> devices = new HashMap<>();


	//------------------------------------------------------------------------------
	// W trakcie projektowania ...

	// należy odwzorować
	// wejścia
	// wyjścia
	// przestrzeń danych db
	// markery
	// timery
	// liczniki

	// W pierwszej kolejności odwzoruję:
	// db, in, out
	// class MemoryMap zawiera elementy klasy MemoryCell






}
