package pjpl.s7.process;

import pjpl.s7.common.MemoryCode;
import pjpl.s7.common.TypeCode;
import pjpl.s7.util.MemoryCell;
import pjpl.s7.util.MemoryMap;


public class MemoryDb extends MemoryMap{
	@Override
	public void init() {

		memoryMap.add(
				MemoryCode.ZMIENNA_1
				, new MemoryCell(
						"zmienna_1"
						, ""
						, pos(TypeCode.size[TypeCode.WORD])
						, TypeCode.WORD
						, 1
						, null
		));
		memoryMap.add(
				MemoryCode.ZMIENNA_2
				, new MemoryCell(
						"zmienna_2"
						, ""
						, pos(TypeCode.size[TypeCode.WORD])
						, TypeCode.WORD
						, 1
						, null
		));
		memoryMap.add(
				MemoryCode.ZMIENNA_3
				, new MemoryCell(
						"zmienna_3"
						, ""
						, pos(TypeCode.size[TypeCode.WORD])
						, TypeCode.WORD
						, 1
						, null
		));
	}
}
