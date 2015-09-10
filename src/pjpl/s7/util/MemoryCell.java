package pjpl.s7.util;

import pjpl.s7.common.TypeCode;
import pjpl.s7.device.PLC;

/**
 * Informacja o mapowaniu zminnej procesowej na sterownik i obszar pamięci
 */
public class MemoryCell {

	public MemoryCell(String name, String description, int pos, int typ, int plcId, PLC plc){
		this.pos = pos;
		this.typ = typ;
		this.name = name;
		this.description = description;
		this.plcId = plcId;
		this.plc = plc;
	}
	public MemoryCell(String name, int pos, int typ, int plcId, PLC plc){
		this.pos = pos;
		this.typ = typ;
		this.name = name;
		this.description = "";
		this.plcId = plcId;
		this.plc = plc;
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
	// pozycja pierwszego bitu w obszarze pamięci
	public int pos;
	// typ danych
	public int typ;
	// nazwa zmiennej
	public String name;
	// opis zmiennej
	public String description;
	// identyfikator sterownika
	public int plcId; // @todo nie wiem czy jest konieczny ten identyfikator
	// sterownik obsługujący tą zmienną
	public PLC plc;

}
