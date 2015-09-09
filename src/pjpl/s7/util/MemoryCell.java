package pjpl.s7.util;

import pjpl.s7.device.PLC;

/**
 * Informacja o mapowaniu zminnej procesowej na sterownik i obszar pamięci
 */
public class MemoryCell {
	// pozycja pierwszego bitu w obszarze pamięci
	public int pos;
	// typ tanych
	public int typ;
	// nazwa zmiennej
	public String name;
	// opis zmiennej
	public String description;
	// identyfikator sterownika
	public int plcId; // @todo nie wiem czy jest konieczny ten identyfikator
	// sterownik obsługujący tą zmienną
	public PLC plc;

	public MemoryCell(String name, String description, int pos, int typ, int plcId, PLC plc){
		this.pos = pos;
		this.typ = typ;
		this.name = name;
		this.description = description;
		this.plcId = plcId;
		this.plc = plc;
	}
}
