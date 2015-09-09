package pjpl.s7.process;

import java.util.ArrayList;
import pjpl.s7.common.MemoryCode;
import pjpl.s7.common.TypeCode;
import pjpl.s7.util.MemoryCell;
import pjpl.s7.util.MemoryMap;

public class MemoryOut extends MemoryMap{

	@Override
	public void init() {
		memoryMap.add(
				MemoryCode.OUT_1
				, new MemoryCell(
						"out_1"
						, ""
						, pos(TypeCode.size[TypeCode.WORD])
						,	TypeCode.BYTE
						, 1
						, null
		));
	}

}
