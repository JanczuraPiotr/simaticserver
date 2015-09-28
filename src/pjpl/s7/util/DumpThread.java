package pjpl.s7.util;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Pobiera zrzut zmiennych procesu i zapisuje je za pomocą DumpExpert.
 * Metoda i miejsce zapisania zrzutu zależy od klasy obiektu DunpExpert.
 *
 * @author Piotr Janczura <piotr@janczura.pl>
 */
public class DumpThread extends Thread{
	/**
	 * @param memClip Bloki danych procesu z których ma być wykonywany zrzut.
	 * @param dumpExpert Obiekt klasy wyspecjalizowanej w zapisie zrzutu.
	 */
	public DumpThread(MemClip memClip, DumpExpert dumpExpert){
		this.memClip = memClip;
		this.dumpExpert = dumpExpert;
		this.queueDump = new LinkedBlockingQueue<MemByteClip>();
		start();
	}
	private DumpThread(){}

	public void run(){
		MemByteClip tmp;
		while(true){
			try {
//				System.out.println(dumpExpert.getClass().getName()+".run() -> czekanie na dane");
				tmp = queueDump.take();
//				System.out.println("DumpThread.run() -> odczytano dane");
				dumpExpert.dump(tmp);

			} catch (InterruptedException ex) {
				Logger.getLogger(DumpThread.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	/**
	 * Metoda musi być wykonana przez steepRead() w klasie dziedziczącej po Process.
	 */
	public void newData(){
		queueDump.add(memClip.toMemByteClip());
	}

	//------------------------------------------------------------------------------
	// atrybuty chronione



	//------------------------------------------------------------------------------
	// atrybuty prywatne

	// Obiekty zebrane w memoryClip odnoszą się do bloków danych w obiekcie klasy Process.
	// Za każdym wykonaniem metody this.newData() na podstawie tych obiektów przygotowywane są bufory które w kolejnym
	// kroku zostaną zrzucone do bazy, pliku, wysłane "gdzieś"
	private MemClip memClip;

	// Obiekt który będzie pobierał dane przygotowane do zrzutu i zebrane w kolejce : ??? jakiej
	private DumpExpert dumpExpert;

	// Kolejka do której wstawiane są zrzuty pamięci przez metodę this.newData()
	private LinkedBlockingQueue<MemByteClip> queueDump;

	//------------------------------------------------------------------------------
	// poniższe chyba nie

	// Bufory tymczasowe na bloki danych przekopiwane z processMemoryX przed wstawieniem ich do kolejki
	// w której oczekują na zapis przez obiekt dumpReceiver.
	private byte[] tmpBuffD;
	private byte[] tmpBuffI;
	private byte[] tmpBuffQ;


}
