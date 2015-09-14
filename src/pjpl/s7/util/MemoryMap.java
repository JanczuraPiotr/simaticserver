package pjpl.s7.util;

import Moka7.S7;
import java.util.TreeMap;
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
		mapById = new TreeMap<>();
		mapByName = new TreeMap<>();
		init();
	}

	public void addCell(int cellCode, MemoryCell cell){
		mapById.put(cellCode, cell);
		mapByName.put(cell.getName(),cell);
	}
	protected void init(){
		initCells();
		initMem();
	}
	public void write(int cellCode, byte val){
		MemoryCell cell = mapById.get(cellCode);
		byte[] m = getMem(cell.getPlcId());
		m[cell.getPos()] = val;
	}
	public void write(int cellCode, short val){
		MemoryCell cell = mapById.get(cellCode);
		byte[] m = getMem(cell.getPlcId());
		S7.SetWordAt(m, cell.getPos(), val);
	}
	public void write(String name, byte val){
		MemoryCell cell = mapByName.get(name);
		byte[] m = getMem(cell.getPlcId());
		m[cell.getPos()] = val;
	}
	public void write(String name, short val){
		MemoryCell cell = mapByName.get(name);
		byte[] m = getMem(cell.getPlcId());
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

	/**
	 * Kod obszaru obsługowanego przez obiekt.
	 * Typ tego obszaru "zakodowany" jest klasą obiektu ale może być konieczne podanie wartości liczbowej oznaczającej
	 * obszar pamięci obowiązujący w bibliotece Moka7
	 * @return
	 */
	abstract protected int areaCode();
	/**
	 * Kod obaszaru wewnątrz D.
	 * Na razie nie wiem jak się tym posłużyć i wszędzie zwracam 0;
	 * @return
	 */
	abstract protected int dbNumber();
	/**
	 * Zwraca bufor zrzutu pamięci dla procesora podanego jako parametr wywołania
	 * @param plc
	 * @return
	 */
	public byte[] getMem(int plcId){
		byte[] m = mem.get(plcId);

//		if( m != null ){
//			m = new byte[memSize];
//			plcs.get(plcId).ReadArea(areaCode(), dbNumber(), 0, memSize, m);
//			mem.put(plcId, m);
//		}

		return m;
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

	// Sterowniki których pamięć przchowywana jest w tym obiekcie
	protected TreeMap<Integer, PLC> plcs;

	// Zrzuty bloków pamięci z procesorów.
	// Kluczem jest identyfikator odpowiadający identyfikatorowi sterownika umieszczonego w plcs.
	// Zrzut wykonywany jest tylko przy pierwszej próbie odczytu w tym cyklu.
	// Na koniec każdego cyklu wszystkie bloki są czyszczone.
	private final TreeMap<Integer, byte[]> mem;

	// Obie kolekcje przechowują ten sam zbiór dla ułatwienia dostępu przez id zmiennej i nazwę zmiennej.
	private final TreeMap<Integer, MemoryCell> mapById;
	private final TreeMap<String, MemoryCell> mapByName;

	// Aktualna pozycja pierwszego bajtu dla następnej zmiennej.
	private int currentPos;

}
/**
 * @prace 01 oznaczać komórki zmienione podczas steep
 * @prace 02 zapisać do sterownika komórki oznaczone jako zmienione
 */