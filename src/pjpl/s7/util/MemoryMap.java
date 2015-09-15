package pjpl.s7.util;

import Moka7.S7;
import java.util.TreeMap;
import javafx.util.Pair;
import pjpl.s7.device.PLC;

/**
 * Mapuje zmienne procesowe na bloki danych sterowników obsługujących proces.
 * Jako klasa abstrakcyjna nie rozróżnia typu zbiorów danych, pamięć danych, wejścia, wyjścia. W celu specjalizowania
 * na te obszary utworzone są klasy pochodne MemoryD, MemoryI, MemoryQ
 */
abstract public class MemoryMap {

	public MemoryMap(TreeMap<Integer, PLC> plcs){
		this.plcs = plcs;
		currentPos = 0;
		mem = new TreeMap<>();
		cellById = new TreeMap<>();
		cellByName = new TreeMap<>();
		init();
	}
	public void addCell(int cellCode, MemoryCell cell){
		cellById.put(cellCode, cell);
		cellByName.put(cell.getName(),cell);
	}
	public void write(int cellCode, byte val){
		MemoryCell cell = cellById.get(cellCode);
		byte[] m = getMemReference(cell.getPlcId());
		m[cell.getPos()] = val;
	}
	public void write(int cellCode, short val){
		MemoryCell cell = cellById.get(cellCode);
		byte[] m = getMemReference(cell.getPlcId());
		S7.SetWordAt(m, cell.getPos(), val);
	}
	public void write(String name, byte val){
		MemoryCell cell = cellByName.get(name);
		byte[] m = getMemReference(cell.getPlcId());
		m[cell.getPos()] = val;
	}
	public void write(String name, short val){
		MemoryCell cell = cellByName.get(name);
		byte[] m = getMemReference(cell.getPlcId());
		S7.SetWordAt(m, cell.getPos(), val);

	}
	public byte readByte(int cellCode){
		return 1;
	}
	public int readShort(int cellCode){
		return 2;
	}
	public byte readByte(String name){
		return 1;
	}
	public int readShort(String name){
		return 2;
	}


	protected void init(){
		initCells();
		initMem();
	}


	/**
	 * Kod obszaru obsługiwanego przez obiekt.
	 * Typ tego obszaru "zakodowany" jest klasą obiektu ale może być konieczne podanie wartości liczbowej oznaczającej
	 * obszar pamięci obowiązujący w bibliotece Moka7
	 * @return
	 */
	abstract protected int areaCode();
	/**
	 * Kod obszaru wewnątrz D.
	 * Na razie nie wiem jak się tym posłużyć i wszędzie zwracam 0;
	 * @return
	 */
	abstract protected int dbNumber();
	/**
	 * Zwraca całą pamięć danego typu w jednym buforze byte[].
	 * Puki zmienne procesu są przechowywane w tej klasie to
	 * @return
	 */
	public byte[] getMemCopy(){
		byte[] memCopy;

		return memCopy;
	}
	/**
	 * Zwraca kopię obszaru pamięci dla procesora o wskazanym identyfikatorze
	 * @param plcId
	 * @return
	 */
	public byte[] getMemCopy(int plcId){
		int size = plcs.get(plcId).getSizeArea(areaCode());
		byte[] memArea = getMemReference(plcId);
 		byte[] memCopy = new byte[size];
		System.arraycopy(memArea, 0, memCopy, 0, size);
		return memCopy;
	}
	/**
	 * Zwraca bufor zrzutu pamięci dla procesora podanego jako parametr wywołania
	 * @param plc
	 * @return
	 */
	public byte[] getMemReference(int plcId){
		return mem.get(plcId);
	}
	/**
	 * Musi być napisane tak by do memmoryMap wstawiać kolejne obiekty opisujące zmienne procesu.
	 * Wypełnić za pomocą wywołania metod addCell dla każdej dodawanej komórki
	 */
	abstract protected void initCells();
	/**
	 * Na podstawie plcs tworzone są tablice na dane pobierane z tych sterowników.
	 */
	private void initMem(){
		plcs.entrySet().stream().forEach((el) -> {
			mem.put(el.getKey(), new byte[el.getValue().getSizeArea(areaCode())]);
		});
	}
	/**
	 * Zwraca pozycję w bloku pamięci na którym umieszczona będzie zmienna, domyślnie wypełniają tablice w sposób ciągły.
	 * @param size Rozmiar obecnie wstawianej zmiennej
	 * @return
	 */
	protected int pos(int size){
		int pos = currentPos;
		currentPos += size;
		return pos;
	}

	//------------------------------------------------------------------------------

	// Sterowniki których pamięć przechowywana jest w tym obiekcie
	protected TreeMap<Integer, PLC> plcs;
	// el.key = identyfikator sterownika
	// el.val.key = index pierwszj komórki pamięci dla sterownika el.key
	// el.val.val = rozmiar obszaru pamięci dla sterownika el.key
	protected TreeMap<Integer, Pair<Integer, Integer> > memDimension;
	// obszar danych procesowych
//	protected byte[] mem;
	// Obie kolekcje przechowują ten sam zbiór dla ułatwienia dostępu przez id zmiennej i nazwę zmiennej.
	private final TreeMap<Integer, MemoryCell> cellById;
	private final TreeMap<String, MemoryCell> cellByName;


	// Zrzuty bloków pamięci z procesorów.
	// Kluczem jest identyfikator odpowiadający identyfikatorowi sterownika umieszczonego w plcs.
	// Zrzut wykonywany jest tylko przy pierwszej próbie odczytu w tym cyklu.
	private final TreeMap<Integer, byte[]> mem;
	// @prace 00 Przerobić mem tak aby była jedną przestrzenią a nie mapą buforów

	// Aktualna pozycja pierwszego bajtu dla następnej zmiennej.
	private int currentPos;

}
/**
 * @prace 00 pamięć zostaje podzielona na mapę byforów
 * @prace 00 pos() musi uwzględniać że przy zmianie procesora zmienia się pozycja.
 * @prace 01 oznaczać komórki zmienione podczas steep
 * @prace 02 zapisać do sterownika komórki oznaczone jako zmienione
 */