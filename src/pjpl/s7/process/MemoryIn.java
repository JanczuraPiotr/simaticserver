package pjpl.s7.process;

import java.util.ArrayList;
import pjpl.s7.common.MemoryCode;
import pjpl.s7.common.TypeCode;
import pjpl.s7.util.MemoryCell;
import pjpl.s7.util.MemoryMap;

public class MemoryIn extends MemoryMap{

	@Override
	public void init() {
		memoryMap.add(
				MemoryCode.IN_1
				, new MemoryCell(
						"in_1"
						, ""
						, pos(TypeCode.size[TypeCode.BYTE])
						, TypeCode.BYTE
						, 	1
						, null
		));

	}

}
