package pjpl.s7.process;

import java.util.HashMap;

/**
 * @author Piotr Janczura <piotr@janczura.pl>
 */
abstract public class Process implements Runnable{
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
	abstract protected void initDb();
	abstract protected void initIn();
	abstract protected void initOut();

	protected String[] dbName;
	protected String[] dbOpis;
	protected int[] dbPos;
	protected int[] dbSize;
	protected int[] dbPlc;
	protected int[] in;
	protected int[] out;
	protected byte[][] bitDb;
	protected byte[][] bitIn;
	protected byte[][] bitOut;
}
