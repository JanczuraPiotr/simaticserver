package pjpl.s7.util;

import pjpl.s7.common.TypeCode;
import pjpl.s7.device.PLC;

/**
 * Informacja o mapowaniu zmiennej procesowej na sterownik i obszar pamięci.
 * Podczas konstrukcji należy wstawiać zmienne w takiej kolejności w jakiej są umieszczone w pamięci plc wraz z
 * zachowaniem kolejności sterowników.
 * @prace 10 MemCell powinna być fabryką zwracającą obiekt klasy odpowiadającej typowi zmiennej wynikającego z typ
 */
public class MemCell {

	/**
	 * Wstawia definicję zmiennej procesowej do mapy pamięci.
	 * @param name Nazwa zmiennej
	 * @param description Opis zmiennej, np w celu wyświetlania podpowiedzi w interfejsie graficznym
	 * @param pos pozycja w buforze gdzie umieszczony jest pierwszy bajt tej zmiennej.
	 *         Należy pobierać z memoryMap.pos(TypeCode.size[TypeCode.xxx])
	 * @param typ itentyfikator typu zmiennej z TypeCode.xxx
	 * @param plcId identyfikator sterownika
	 * @param plc  wskaźnik do sterownika
	 */
	public MemCell(String name, String description, int pos, int typ, int plcId, PLC plc){
		this.pos = pos;
		this.typ = typ;
		this.name = name;
		this.description = description;
		this.plcId = plcId;
		this.plc = plc;
		this.size = TypeCode.size[typ];
	}
	/**
	 * Wstawia definicję zmiennej procesowej do mapy pamięci.
	 * @param name Nazwa zmiennej
	 * @param pos pozycja w buforze gdzie umieszczony jest pierwszy bajt tej zmiennej. Należu pobierać z MemoryMap.pos()
	 * @param typ itentyfikator typu zmiennej z TypeCode.xxx
	 * @param plcId identyfikator sterownika
	 * @param plc  wskaźnik do sterownika
	 *
	 */
	public MemCell(String name, int pos, int typ, int plcId, PLC plc){
		this(name, "", pos, typ, plcId, plc);
	}

	/**
	 * Pozycja pierwszego bajtu zmiennej w tablicy pamięci.
	 * @return
	 */
	public int getPos(){
		return pos;
	}
	/**
	 * Typ zmiennej według S7
	 * @return
	 */
	public int getTyp(){
		return typ;
	}
	/**
	 * Nazwa zmiennej, taka jak zapisana w sterowniku
	 * @return
	 */
	public String getName(){
		return name;
	}
	/**
	 * Dodatkowy opis zmiennej do użycia w interfejsie graficznym.
	 * @return
	 */
	public String getDescription(){
		return description;
	}
	/**
	 * Identyfikator sterownika
	 * @return
	 */
	public int getPlcId(){
		return plcId;
	}
	/**
	 * Sterownik zawierający tą zmienną.
	 * @return
	 */
	public PLC getPlc(){
		return plc;
	}
	/**
	 * Ilość bajtów zajmowanych przez zmienną.
	 * @return
	 */
	public int getSize(){
		return TypeCode.size[getTyp()];
	}

	//------------------------------------------------------------------------------
	// pozycja pierwszego bitu w obszarze pamięci. Nie jest pozycją w wewnętrznej pamięci PLC
	public int pos;
	// typ danych
	public int typ;
	public int size;
	// nazwa zmiennej
	public String name;
	// opis zmiennej
	public String description;
	// identyfikator sterownika
	public int plcId; // @todo nie wiem czy jest konieczny ten identyfikator
	// sterownik obsługujący tą zmienną
	public PLC plc;

	private byte BIT;
	private short INT;
	private int DINT;
	private float REAL;
	private double LREAL;


}
