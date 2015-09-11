package pjpl.s7.util;

import Moka7.S7;
import java.util.HashMap;
import java.util.TreeMap;
import pjpl.s7.device.PLC;

/**
 * Mapuje zmienne procesowe na bloki danych sterowników obsługujących proces.
 */
abstract public class MemoryMap {
	public MemoryMap(HashMap<Integer, PLC> plcs){
		this.plcs = plcs;
		currentPos = 0;
		mapById = new TreeMap<>();
		mapByName = new TreeMap<>();
		init();
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
	public void addCell(int cellCode, MemoryCell cell){
		mapById.put(cellCode, cell);
		mapByName.put(cell.getName(),cell);
		memSize += cell.getSize();
	}
	abstract protected int area();
	abstract protected int dbNumber();
	/**
	 * Zwraca bufor zrzutu pamięci dla procesora podanego jako parametr wywołania
	 * @param plc
	 * @return
	 */
	private byte[] getMem(int plcId){
		byte[] m = mem.get(plcId);

		if( m != null ){
			m = new byte[memSize];
			plcs.get(plcId).ReadArea(area(), dbNumber(), 0, memSize, m);
			mem.put(plcId, m);
		}

		return m;
	}
	/**
	 * Musi być napisane tak by do memmoryMap wstawiać kolejne obiekty opisujące zmienne procesu.
	 */
	abstract protected void init();
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

	/**
	 * Aktualna pozycja pierwszego bajtu dla następnej zmiennej.
	 */
	private int currentPos;
	protected HashMap<Integer, PLC> plcs;

	/**
	 * Zrzuty bloków pamięci z procesorów.
	 * Kluczem jest identyfikator odpowiadający identyfikatorowi sterownika umieszczonego w plcs.
	 * Zrzut wykonywany jest tylko przy pierwszej próbie odczytu w tym cyklu.
	 * Na koniec każdego cyklu wszystkie bloki są czyszczone.
	 */
	private final TreeMap<Integer, byte[]> mem;
	private int memSize;

	private final TreeMap<Integer, MemoryCell> mapById; // @todo zoptymalizować typ kontenera
	private final TreeMap<String, MemoryCell> mapByName;


	{
		mem = new TreeMap<>();
	}

}
