package pjpl.s7.process;

import java.util.HashMap;
import pjpl.s7.util.MemoryMap;

/**
 */
 public abstract class Process implements Runnable{

	/**
	 * Operacje wykonywane podczas każdego uruchomienia wątku.
	 * 1. Odczyt zmiennych
	 *		- wszystkich gdy wączone jest logowanie ich stanu
	 *		- odczyt tylko zmiennych do obsługi Komendy gdy nie ma logowania
	 * 2. Wykonanie komendy
	 * 3. Zapis zmiennych zmodyfikowanych przez komendę.
	 */
	abstract public void steep();
	abstract public void steepException(Exception e);
	abstract public void steepExceptionFinally(Exception e);
	abstract public void steepFinaly();

	@Override
	public void run(){
		try{
			steep();
		}catch(Exception e){
			try{
				steepException(e);
			}catch(Exception eBis){
				steepExceptionFinally(e);
				System.out.println("Process.run nie udana próba obsłużenia wyjątku Exception eBis");
			}
		}finally{
			steepFinaly();
		}
	}


	abstract protected void initMemory();
	abstract protected void initPlcs();

	{
		plcs = new HashMap<>();
		initPlcs();
		initMemory();
	}

	//------------------------------------------------------------------------------

	protected MemoryMap memoryDB;
	protected MemoryIn memoryIn;
	protected MemoryOut memoryOut;
	// @todo opracować pozostałe bloki pamięci.

	protected HashMap<Integer, pjpl.s7.device.PLC> plcs;





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
