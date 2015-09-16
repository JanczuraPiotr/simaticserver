package pjpl.s7.process;

import Moka7.S7;
import pjpl.s7.common.CellCode;
import pjpl.s7.common.ConstPLC;
import pjpl.s7.common.TypeCode;
import pjpl.s7.device.PLC;
import pjpl.s7.util.MemCell;
import pjpl.s7.util.MemMap;


public class MemD extends MemMap{

	public MemD(PLC[] plcs) {
		super(plcs);
	}
	@Override
	public void addCells() {
		addCell(CellCode.ZMIENNA_1
				, new MemCell(
						"zmienna_1"
						, pos(TypeCode.size[TypeCode.INT])
						, TypeCode.INT
						, ConstPLC.PLC1
						, plcs[ConstPLC.PLC1]
		));
		addCell(CellCode.ZMIENNA_2
				, new MemCell(
						"zmienna_2"
						, "opis zmiennej zmienna_2"
						, pos(TypeCode.size[TypeCode.INT])
						, TypeCode.INT
						, ConstPLC.PLC1
						, plcs[ConstPLC.PLC1]
		));
		addCell(CellCode.ZMIENNA_3
				, new MemCell(
						"zmienna_3"
						, pos(TypeCode.size[TypeCode.INT])
						, TypeCode.INT
						, ConstPLC.PLC1
						, plcs[ConstPLC.PLC1]
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
