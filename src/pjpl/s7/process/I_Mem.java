package pjpl.s7.process;

import Moka7.S7;
import pjpl.s7.common.VarCode;
import pjpl.s7.common.ConstPLC;
import pjpl.s7.common.TypeCode;
import pjpl.s7.device.PLC;
import pjpl.s7.util.MemCell;
import pjpl.s7.util.MemMap;

public class I_Mem extends MemMap{

	public I_Mem(PLC[] plcs) {
		super(plcs);
	}

	@Override
	public void addCells() {
		addCell(VarCode.IN_1
				, new MemCell(
						"in_1"
						, pos(TypeCode.occupiedMemSpace[TypeCode.PORT])
						, TypeCode.PORT
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
