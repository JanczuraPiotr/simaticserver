package pjpl.s7.process;

import Moka7.S7;
import java.util.TreeMap;
import pjpl.s7.common.CellCode;
import pjpl.s7.common.TypeCode;
import pjpl.s7.util.MemoryCell;
import pjpl.s7.util.MemoryMap;


public class MemoryD extends MemoryMap{

	public MemoryD(TreeMap<Integer, pjpl.s7.device.PLC> plcs) {
		super(plcs);
	}
	@Override
	public void initCells() {
		addCell(
				CellCode.ZMIENNA_1
				, new MemoryCell(
						"zmienna_1"
						, pos(TypeCode.size[TypeCode.WORD])
						, TypeCode.WORD
						, Process1.PLC1
						, plcs.get(Process1.PLC1)
		));
		addCell(
				CellCode.ZMIENNA_2
				, new MemoryCell(
						"zmienna_2"
						, "opis zmiennej zmienna_2"
						, pos(TypeCode.size[TypeCode.WORD])
						, TypeCode.WORD
						, Process1.PLC1
						, plcs.get(Process1.PLC1)
		));
		addCell(
				CellCode.ZMIENNA_3
				, new MemoryCell(
						"zmienna_3"
						, pos(TypeCode.size[TypeCode.WORD])
						, TypeCode.WORD
						, Process1.PLC1
						, plcs.get(Process1.PLC1)
		));
	}

	@Override
	protected int areaCode() {
		return S7.D;
	}

	@Override
	protected int dbNumber() {
		return 0;
	}
}
