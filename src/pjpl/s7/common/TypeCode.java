package pjpl.s7.common;


public class TypeCode {
	public static final int BIT = 0;
	public static final int BYTE = 1;
	public static final int WORD = 2;

	public static final int[] size = new int[3];
	static{
		size[BYTE] = 1;
		size[WORD] = 2;
	}

	public static final String[] name = new String[3];
	static{
		name[BYTE] = "byte";
		name[WORD] = "word";
	}
}
