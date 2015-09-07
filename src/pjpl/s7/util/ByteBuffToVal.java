package pjpl.s7.util;

import java.io.DataInputStream;

/**
 * @author Piotr Janczura <piotr@janczura.pl>
 */
public class ByteBuffToVal {

	public static boolean _boolean(DataInputStream input){
		return true;
	}

	public static boolean _boolean(byte[] buff, int start){
		return true;
	}
	public static void _boolean(boolean val, byte[] buff, int start){

	}
	public static boolean _boolean(byte[] buff, int start, int pos){
		return true;
	}
	public static void _boolean(boolean val, byte[] buff, int start, int pos){

	}
	public static char _char(byte[] buff, int start){
		return ' ';
	}
	public static void _char(char val, byte[] buff, int start){

	}
	public static byte _byte(byte[] buff, int start){
		return 1;
	}
	public static void _byte(byte val, byte[] buff, int start){

	}
	public static short _short(byte[] buff, int start){
		return 1;
	}
	public static void _short(short val, byte[] buff, int start){

	}
	public static int _int(byte[] buff, int start){
		return 1;
	}
	public static void _int(int val, byte[] buff, int start){

	}
	public static long _long(byte[] buff, int start){
		return 1;
	}
	public static void _long(long val, byte[] buff, int start){

	}
	public static float _float(byte[] buff, int start){
		return 1;
	}
	public static void _float(float val, byte[] buff, int start){

	}
	public static double _double(byte[] buff, int start){
		return 1;
	}
	public static void _double(double val, byte[] buff, int start){

	}
}
