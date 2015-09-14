package pjpl.s7.process;

import Moka7.S7;
import java.util.TreeMap;
import pjpl.s7.common.CellCode;
import pjpl.s7.common.TypeCode;
import pjpl.s7.device.PLC;
import pjpl.s7.util.MemoryCell;
import pjpl.s7.util.MemoryMap;

public class MemoryI extends MemoryMap{

	public MemoryI(TreeMap<Integer, PLC> plcs) {
		super(plcs);
	}

	@Override
	public void initCells() {
		addCell(
				CellCode.IN_1
				, new MemoryCell(
						"in_1"
						, pos(TypeCode.size[TypeCode.BYTE])
						, TypeCode.BYTE
						, Process1.PLC1
						, plcs.get(Process1.PLC1)
		));
	}

	@Override
	protected int areaCode() {
		return S7.I;
	}

	@Override
	protected int dbNumber() {
		return 0;
	}

}
