package pjpl.s7.process;

import Moka7.S7;
import java.util.HashMap;
import pjpl.s7.common.CellCode;
import pjpl.s7.common.TypeCode;
import pjpl.s7.util.MemoryCell;
import pjpl.s7.util.MemoryMap;

public class MemoryOut extends MemoryMap{

	public MemoryOut(HashMap<Integer, pjpl.s7.device.PLC> plcs) {
		super(plcs);
	}

	@Override
	public void init() {
		addCell(
				CellCode.OUT_1
				, new MemoryCell(
						"out_1"
						, pos(TypeCode.size[TypeCode.WORD])
						,	TypeCode.BYTE
						, Process1.PLC1
						, plcs.get(Process1.PLC1)
		));
	}

	@Override
	protected int area() {
		return S7.Q;
	}

	@Override
	protected int dbNumber() {// @todo określić sposób wyznaczania
		return 0;
	}

}
