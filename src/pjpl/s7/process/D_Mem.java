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
		addCell(VarCode.ZMIENNA_1
				, new MemCell(
						"zmiennaByte"
						, pos(TypeCode.occupiedMemSpace[TypeCode.BYTE])
						, TypeCode.BYTE
						, ConstPLC.PLC1
						, plcs[ConstPLC.PLC1]
				)
		);
		addCell(VarCode.ZMIENNA_2
				, new MemCell(
						"zmiennaInt"
						, "opis zmiennej zmiennaInt"
						, pos(TypeCode.occupiedMemSpace[TypeCode.INT])
						, TypeCode.INT
						, ConstPLC.PLC1
						, plcs[ConstPLC.PLC1]
				)
		);
		addCell(VarCode.ZMIENNA_3
				, new MemCell(
						"zmiennaDInt"
						, pos(TypeCode.occupiedMemSpace[TypeCode.DINT])
						, TypeCode.DINT
						, ConstPLC.PLC1
						, plcs[ConstPLC.PLC1]
				)
		);
		addCell(VarCode.ZMIENNA_4
				,	new MemCell(
						"zmiennaReal"
						, "opis dla zmiennej zmiennaReal"
						, pos(TypeCode.occupiedMemSpace[TypeCode.REAL])
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
