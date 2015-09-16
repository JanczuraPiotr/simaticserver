package pjpl.s7.process;

import Moka7.S7;
import pjpl.s7.common.CellCode;
import pjpl.s7.common.ConstPLC;
import pjpl.s7.common.TypeCode;
import pjpl.s7.device.PLC;
import pjpl.s7.util.MemCell;
import pjpl.s7.util.MemMap;

public class MemI extends MemMap{

	public MemI(PLC[] plcs) {
		super(plcs);
	}

	@Override
	public void addCells() {
		addCell(CellCode.IN_1
				, new MemCell(
						"in_1"
						, pos(TypeCode.size[TypeCode.BYTE])
						, TypeCode.BYTE
						, ConstPLC.PLC1
						, plcs[ConstPLC.PLC1]
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
