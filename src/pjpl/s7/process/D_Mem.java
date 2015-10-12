package pjpl.s7.process;

import Moka7.S7;
import pjpl.s7.common.VarCode;
import pjpl.s7.common.ConstPLC;
import pjpl.s7.common.TypeCode;
import pjpl.s7.device.PLC;
import pjpl.s7.util.MemCell;
import pjpl.s7.util.MemMap;


public class D_Mem extends MemMap{

	public D_Mem(PLC[] plcs) {
		super(plcs);
	}
	@Override
	public void addCells() {
		addCell(VarCode.ZMIENNA_BYTE
				, new MemCell(
						"zmiennaByte"
						, pos(TypeCode.size[TypeCode.BYTE])
						, TypeCode.BYTE
						, ConstPLC.PLC1
						, plcs[ConstPLC.PLC1]
				)
		);
		addCell(VarCode.ZMIENNA_INT
				, new MemCell(
						"zmiennaInt"
						, "opis zmiennej zmiennaInt"
						, pos(TypeCode.size[TypeCode.INT])
						, TypeCode.INT
						, ConstPLC.PLC1
						, plcs[ConstPLC.PLC1]
				)
		);
		addCell(VarCode.ZMIENNA_DINT
				, new MemCell(
						"zmiennaDInt"
						, pos(TypeCode.size[TypeCode.DINT])
						, TypeCode.DINT
						, ConstPLC.PLC1
						, plcs[ConstPLC.PLC1]
				)
		);
		addCell(VarCode.ZMIENNA_REAL
				,	new MemCell(
						"zmiennaReal"
						, "opis dla zmiennej zmiennaReal"
						, pos(TypeCode.size[TypeCode.REAL])
						, TypeCode.REAL
						, ConstPLC.PLC1
						, plcs[ConstPLC.PLC1]
				)
		);
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
