package pjpl.s7.util;

import Moka7.S7;
import java.util.Map;
import java.util.TreeMap;
import pjpl.s7.common.ConstPLC;
import pjpl.s7.common.TypeCode;
import pjpl.s7.device.PLC;

/**
 * Mapuje zmienne procesowe na bloki danych sterowników obsługujących proces.
 * Jako klasa abstrakcyjna nie rozróżnia typu zbiorów danych, pamięć danych, wejścia, wyjścia. W celu specjalizowania
 * na te obszary utworzone są klasy pochodne MemoryD, MemoryI, MemoryQ
 */
abstract public class MemMap {

	protected static final int PLC_MEM_ARRANGE_START = 0;
	protected static final int PLC_MEM_ARRANGE_SIZE = 1;

	public MemMap(PLC[] plcs){
		this.plcs = plcs;
		currentPos = 0;
		tempCellMap = new TreeMap<>();

		init();
	}

	public int getSize(){
		return memSize;
	}
	public void write(int cellCode, int val){
		switch(cells[cellCode].typ){
			case TypeCode.BYTE:
				writeByte(cellCode, (byte)val);
				break;
			case TypeCode.INT:
				writeInt(cellCode, (short)val);
				break;
			case TypeCode.DINT:
				writeDInt(cellCode, val);
				break;
			default:
				// @todo rzuć wyjątek
		}
	}
	public void write(int cellCode, float val){
		switch(cells[cellCode].typ){
			case TypeCode.REAL:
				break;
			case TypeCode.LREAL:
				break;
			default:
				// @todo rzuć wyjątek
		}
	}
	public void write(int cellCode, double val){
		switch(cells[cellCode].typ){
			case TypeCode.LREAL:
				break;
			default:
				// @todo rzuć wyjątek
		}
	}
	public byte readByte(int cellCode){
		return mem[cells[cellCode].pos];
	}
	public short readInt(int cellCode){
		return (short)S7.GetShortAt(mem, cells[cellCode].pos);
	}
	public int readDInt(int cellCode){
		return 1;
	}



	/**
	 * Modyfikuje zmienną PLC bajtową 8 bitów
 	 * @param cellCode
	 * @param val
	 */
	private void writeByte(int cellCode, byte val){
		mem[cells[cellCode].pos] = val;
	}
	/**
	 * Modyfikuje zmienną PLC Short 16 bitów ze znakiem
	 * @param cellCode
	 * @param val
	 */
	private void writeInt(int cellCode, short val){
		S7.SetShortAt(mem, cells[cellCode].pos, val);
	}
	/**
	 * Modyfikuje zmienną PLC DInt 32 bity ze znakiem
	 * @param cellCode
	 * @param val
	 */
	private void writeDInt(int cellCode, int val){
		S7.SetDIntAt(mem, cells[cellCode].pos, val);
	}

	protected void addCell(int cellCode, MemCell cell){
		tempCellMap.put(cellCode, cell);
		updateMemArrange(cellCode, cell);
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
	 * Zwraca referencję do bufora przechowującego wszystkie zmienne procesu.
	 * @return
	 */
	public byte[] getMem(){
		return mem;
	}
	/**
	 * Zwraca kopię bufora przechowującego wszystkie zmienne procesu
	 * @return
	 */
	public byte[] getMemCopy(){
		byte[] memCopy = new byte[memSize];
		System.arraycopy(mem, 0, memCopy, 0, memSize);
		return memCopy;
	}
	/**
	 * Zwraca kopię obszaru pamięci dla procesora o wskazanym identyfikatorze
	 * @param plcId
	 * @return
	 */
	public byte[] getMemCopy(int plcId){
 		byte[] memCopy = new byte[memArrange[plcId][PLC_MEM_ARRANGE_SIZE]];
		System.arraycopy(
				mem
				, memArrange[plcId][PLC_MEM_ARRANGE_START]
				, memCopy
				, 0
				, memArrange[plcId][PLC_MEM_ARRANGE_SIZE]
		);
		return memCopy;
	}
	/**
	 * Musi być napisane tak by do memmoryMap wstawiać kolejne obiekty opisujące zmienne procesu.
	 * Wypełnić za pomocą wywołania metod addCell dla każdej dodawanej komórki
	 */
	abstract protected void addCells();
	/**
	 * Zwraca pozycję w bloku pamięci na którym umieszczona będzie zmienna, domyślnie wypełniają tablice w sposób ciągły.
	 * @param size Rozmiar obecnie wstawianej zmiennej
	 * @return
	 */
	final protected int pos(int size){
		int pos = currentPos;
		currentPos += size;
		return pos;
	}

	//------------------------------------------------------------------------------
	// private

	private void init(){
		initMemArarange();
		addCells();
		initArrCells();
		initMem();
//		initMemArarange();
	}
	/**
	 * Na podstawie memSize wyznaczonego podczas wstawiania MemCell
	 */
	private void initMem(){
		mem = new byte[memSize];
	}

	private void initArrCells(){
		cellsCount = tempCellMap.size();
		cells = new MemCell[cellsCount];
		tempCellMap.entrySet().stream().forEach(
				(Map.Entry<Integer, MemCell> el)->{
					cells[el.getKey()] = el.getValue();
				}
		);
	}
	/**
	 * Na podstawie cells inicjuje memArrange
	 */
	private void initMemArarange(){
		// dla plc o identyfikatorze zgodnym z przekazanym w memoryCell zwiększyć rozmiar pamięci
		// dla wszystkich plc leżących w plcs za plc przesunąć index pierwszego bufora pamięci o tą samą wartość.
		for( int c = 0 ; c < cellsCount ; c++ ){
			memArrange[cells[c].plcId][PLC_MEM_ARRANGE_SIZE] += cells[c].size;
			// Dla wszystkich procesorów leżących w dalszej kolejności przesuwa się punkt startowy
			for ( int p = cells[c].plcId + 1 ; p < plcs.length ; p++){
				memArrange[p][PLC_MEM_ARRANGE_START] += cells[c].size;
			}
		}
	}
	private void updateMemArrange(int cellCode, MemCell cell){
		memSize += cell.size;
		memArrange[cell.plcId][PLC_MEM_ARRANGE_SIZE] += cell.size;
		for( int p = cell.plcId + 1 ; p < ConstPLC.COUNT ; p++ ){
			memArrange[p][PLC_MEM_ARRANGE_START] += cell.size;
		}
	}

	//------------------------------------------------------------------------------
	// atrybuty chronione

	// Sterowniki których pamięć przechowywana jest w tym obiekcie
	protected PLC[] plcs;
	// Rozmieszczenie pamięci sterowników "plcs" w buforze danych procesowych "mem"
	// memArrange[plcId][PLC_MEM_ARRANGE_START] index pierwszej komórki pamięci plcId w mem
	// memArrange[plcId][PLC_MEM_ARRANGE_SIZE] rozmiar pamięci w mem przynależnej procesorowi plcId
	protected int[][] memArrange = new int[ConstPLC.COUNT][2];
	// Rozmiar pamięci procesu modyfikowany w metodzie updateMemArrange, służy do utworzenia "mem".
	protected int memSize = 0;
	// obszar danych procesowych
	protected byte[] mem;

	//------------------------------------------------------------------------------
	// atrybuty prywatne

	// Obie kolekcje przechowują ten sam zbiór dla ułatwienia dostępu przez id zmiennej i nazwę zmiennej.
	// cellMapById worzona jest podczas dodawania kolejnych komórek do mapy pamięci. Po dodaniu ostatniej komórki
	// metoda initCells() tworzy roboczą tablicę cells która będzie służyć jako obiekt dostępu do komórek.
	// Mapa cellMapById służy tylko do zebrania listy komórek.
	private final TreeMap<Integer, MemCell> tempCellMap;
	private int cellsCount = 0;
	private MemCell[] cells;

	// Aktualna pozycja pierwszego bajtu dla następnej zmiennej.
	private int currentPos;

}
/**
 * @prace 01 oznaczać komórki zmienione podczas steep
 * @prace 02 zapisać do sterownika komórki oznaczone jako zmienione
 */