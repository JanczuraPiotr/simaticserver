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
	protected static final int PLC_MEM_ARRANGE_ = 2;

	// PLC_MEM_MODIFIED_?? wyznacza obszar przeznaczony do wysyłki do PLC.
	// W jego skład wchodzi obszar przechowujący zmodyfikowane zmienne ale jeżeli zmiennych jest więcej a pomiędzy nimi
	// jest obszar przechowujący zmienne nie zmodyfikowane to ten obszar też zostanie wysłany. W innym przypadku należało
	// by każdą zmienną wysyłać osobno.
	// Pierwszy bajt przechowujący pierwszy bajt zmodyfikowanej zmiennej o najmniejszym kodzie.
	protected static final int PLC_MEM_MODIFIED_FIRST = 0;
	// ostatni bajt przechowujący ostatni bajt zmodyfikowanej zmiennej o największym kodzie.
	protected static final int PLC_MEM_MODIFIED_LAST = 1;
	protected static final int PLC_MEM_MODIFIED_ = 2;


	public MemMap(PLC[] plcs){
		this.plcs = plcs;
		currentPos = 0;
		tempCellMap = new TreeMap<>();

		init();
	}


	//------------------------------------------------------------------------------
	// interfejs - początek

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
	 * Zapisuje do pamięci wewnętrznej podany buff jako nową zawartość całej pamięci.
	 * @param buff
	 */
	public void writeMem(byte[] buff){
		System.arraycopy(buff, 0, mem, 0, memSize);
	}
	/**
	 * Zapisuje do pamięci nową zawartość pamięci procesora plcId
	 * @param buff
	 * @param plcId
	 */
	public void writeMem(byte[] buff, int plcId){
		System.arraycopy(buff, 0, mem, memArrange[plcId][PLC_MEM_ARRANGE_START], memArrange[plcId][PLC_MEM_ARRANGE_SIZE]);
	}
	/**
	 * Odczytuje obszar oznaczony jako przeznaczony do przesłania do sterownika.
	 * @param plcId
	 * @param start
	 * @return
	 */
	public byte[] readModifiedMem(int plcId){
		int start = getStartModifiedMem(plcId);
		int size = getSizeModifiedMem(plcId);
		byte[] buff = new byte[ size ];
		System.arraycopy(mem, start, buff, 0, size);
		return buff;
	}
	/**
	 * Zwraca index komórki w pamięci procesora gdzie znjduje się początek pamięci zawierającej zmodyfikowane zmienne.
	 * @param plcId
	 * @return
	 */
	public int getStartModifiedMem(int plcId){
		return memModified[plcId][PLC_MEM_MODIFIED_FIRST] - memArrange[plcId][PLC_MEM_ARRANGE_START];
	}
	/**
	 * Zwraca rozmiar pamięci procesrora zawierającego zmodyfikowane zmienne.
	 * @param plcId
	 * @return
	 */
	public int getSizeModifiedMem(int plcId){
		return memModified[plcId][PLC_MEM_MODIFIED_LAST] - memModified[plcId][PLC_MEM_MODIFIED_FIRST] + 1;
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
		updateMemModified(cells[cellCode]);
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
		updateMemModified(cells[cellCode]);
	}
	public void write(int cellCode, double val){
		switch(cells[cellCode].typ){
			case TypeCode.LREAL:
				break;
			default:
				// @todo rzuć wyjątek
		}
		updateMemModified(cells[cellCode]);
	}
	public byte readByte(int cellCode){
		return mem[cells[cellCode].pos];
	}
	public int readInt(int cellCode){
		return S7.GetShortAt(mem, cells[cellCode].pos);
	}
	public int readDInt(int cellCode){
		// @prace public int readDInt(int cellCode){
		return 1;
	}
	public float readReal(int cellCode){
		// @prace public float readReal(int cellCode)
		return 1;
	}
	public double readLReal(int cellCode){
		// @prace public double readLReal(int cellCode)
		return 1;
	}

	public int getSize(){
		return memSize;
	}
	public int getPlcMemSize(int plcId){
		return memArrange[plcId][PLC_MEM_ARRANGE_SIZE];
	}

	// interfejs - koniec
	//------------------------------------------------------------------------------

	//------------------------------------------------------------------------------
	// metody chronione

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
		// dla plc o identyfikatorze zgodnym z przekazanym w memCell zwiększyć rozmiar pamięci
		// dla wszystkich plc leżących w plcs za plc przesunąć index pierwszego bufora pamięci o tą samą wartość.

		memSize = 0;

		for( int c = 0 ; c < cellsCount ; c++ ){
			memSize += cells[c].size;
			memArrange[cells[c].plcId][PLC_MEM_ARRANGE_SIZE] += cells[c].size;
			// Dla wszystkich procesorów leżących w dalszej kolejności przesuwa się punkt startowy
			for ( int p = cells[c].plcId + 1 ; p < plcs.length ; p++){
				memArrange[p][PLC_MEM_ARRANGE_START] += cells[c].size;
			}
		}
	}
	/**
	 * Na podstawie cell.size modyfikuje licznik ilości bajtów potrzebnych do obsługi zmiennych zmapowanych w tym obiekcie.
	 * @param cellCode
	 * @param cell
	 */
	private void updateMemArrange(int cellCode, MemCell cell){
		memSize += cell.size;
		memArrange[cell.plcId][PLC_MEM_ARRANGE_SIZE] += cell.size;
		for( int p = cell.plcId + 1 ; p < ConstPLC.COUNT ; p++ ){
			memArrange[p][PLC_MEM_ARRANGE_START] += cell.size;
		}
	}
	private void updateMemModified(MemCell cell){
		if( memModified[cell.plcId][PLC_MEM_MODIFIED_FIRST] > cell.pos){
			memModified[cell.plcId][PLC_MEM_MODIFIED_FIRST] = cell.pos;
		}
		if( memModified[cell.plcId][PLC_MEM_MODIFIED_LAST] < cell.pos + cell.size - 1 ){
			memModified[cell.plcId][PLC_MEM_MODIFIED_LAST] = cell.pos + cell.size - 1;
		}
	}
	private void clearMemArrange(){
		for( int[] plci : memArrange ){
			plci[PLC_MEM_ARRANGE_START] = 0;
			plci[PLC_MEM_ARRANGE_SIZE] = 0;
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
	// Obszary zmodyfikowane przez metody zapisu zmiennych do pamięci procesu.
	// Do PLC zapisywane są bajty pomiędzy pierwszym bajtem zmodyfikowanej zmiennej o najmniejszym kodzie a ostatnim
	// bajtem zmodyfikowanej zmiennej o największym identyfikatorze.
	protected int[][] memModified = new int[ConstPLC.COUNT][2];
	// Licznik ilości bajtów potrzebnych do obsługi zmiennych mapowanych w tym obiekcie.
	protected int memSize = 0;
	// obszar danych procesowych
	protected byte[] mem;

	//------------------------------------------------------------------------------
	// atrybuty prywatne

	// Obie kolekcje przechowują ten sam zbiór dla ułatwienia dostępu przez id zmiennej i nazwę zmiennej.
	// cellMapById włożona jest podczas dodawania kolejnych komórek do mapy pamięci. Po dodaniu ostatniej komórki
	// metoda initCells() tworzy roboczą tablicę cells która będzie służyć jako obiekt dostępu do komórek.
	// Mapa cellMapById służy tylko do zebrania listy komórek.
	private final TreeMap<Integer, MemCell> tempCellMap;
	private int cellsCount = 0;
	private MemCell[] cells;

	// Aktualna pozycja pierwszego bajtu dla następnej zmiennej.
	private int currentPos;

}
/**
 * 
 */