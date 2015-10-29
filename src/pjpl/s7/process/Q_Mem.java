package pjpl.s7.process;

import Moka7.S7;
import pjpl.s7.common.VarCode;
import pjpl.s7.common.ConstPLC;
import pjpl.s7.common.TypeCode;
import pjpl.s7.device.PLC;
import pjpl.s7.util.MemCell;
import pjpl.s7.util.MemMap;

public class Q_Mem extends MemMap{

	public Q_Mem(PLC[] plcs) {
		super(plcs);
	}

	@Override
	public void addCells() {
		addCell(VarCode.OUT_1
				, new MemCell(
						"out_1"
						, pos(TypeCode.occupiedMemSpace[TypeCode.PORT])
						,	TypeCode.PORT
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
