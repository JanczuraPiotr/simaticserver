package pjpl.s7.process;

import Moka7.S7;
import pjpl.s7.common.VarCode;
import pjpl.s7.common.ConstPLC;
import pjpl.s7.common.TypeCode;
import pjpl.s7.device.PLC;
import pjpl.s7.util.MemCell;
import pjpl.s7.util.MemMap;

public class MemQ extends MemMap{

	public MemQ(PLC[] plcs) {
		super(plcs);
	}

	@Override
	public void addCells() {
		addCell(VarCode.OUT_1
				, new MemCell(
						"out_1"
						, pos(TypeCode.size[TypeCode.INT])
						,	TypeCode.BYTE
						, ConstPLC.PLC1
						, plcs[ConstPLC.PLC1]
		));
	}

	@Override
	protected int areaCode() {
		return S7.Q;
	}

	@Override
	protected int dbNumber() {// @todo określić sposób wyznaczania
		return 0;
	}

}
