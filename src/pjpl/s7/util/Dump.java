package pjpl.s7.util;
// Zrzuca zmienne w rozdzieleniu na typy bloków

import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 */
public class Dump extends Thread{

	/**
	 * Tworzy wątek z na obiekcie dumpExpert i uruchamia go.
	 * @param memoryClip
	 * @param dumpExpert
	 */
	public Dump(MemoryClip memoryClip, DumpExpert dumpExpert){
		this.memoryClip = memoryClip;
		this.dumpExpert = dumpExpert;
		this.queueDump = new LinkedBlockingQueue<MemoryByteClip>();
		start();
	}
	private Dump(){}

	public void run(){
		MemoryByteClip tmp;
		while(true){
			try {
				System.out.println("Dump.run() -> czekanie na dane");
				tmp = queueDump.take();
				System.out.println("Dump.run() -> odczytano dane");

			} catch (InterruptedException ex) {
				Logger.getLogger(Dump.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	/**
	 * Metoda musi być wykonana w przez Process.steepRead().
	 * Dzięki temu obiekt jest na bierząco informowany o aktualizowaniu stanu pamięci procesu
	 * i może przystapić do zrzutu.
	 */
	public void newData(){
		System.out.println("Dump.newData()");
		System.out.println("memoryClip.timeStamp = "+memoryClip.timeStamp);
		
		MemoryByteClip tmp = new MemoryByteClip(
				memoryClip.memoryD.getMemCopy()
				, tmpBuffI
				, tmpBuffQ
				, memoryClip.timeStamp
		);

	}

	// Obiekty zebrane w memoryClip odnoszą się do bloków danych w obiekcie klasy Process.
	// Za każdym wykonaniem metody this.newData() na podstawie tych obiektów przygotowywane są bufory które w kolejnym
	// kroku zostaną zrzucone do bazy, pliku, wysłane "gdzieś"
	private MemoryClip memoryClip;

	// Obiekt który będzie pobierał dane przygotowane do zrzutu i zebrane w kolejce : ??? jakiej
	private DumpExpert dumpExpert;

	// Kolejka do której wstawiane są zrzuty pamięci przez metodę this.newData()
	private LinkedBlockingQueue<MemoryByteClip> queueDump;

	//------------------------------------------------------------------------------
	// poniższe chyba nie

	// Bufory tymczasowe na bloki danych przkopowane z processMemoryX przed wstawieniem ich do kolejki
	// w której oczekują na zapis przez obiekt dumpReceiver.
	private byte[] tmpBuffD;
	private byte[] tmpBuffI;
	private byte[] tmpBuffQ;


}
