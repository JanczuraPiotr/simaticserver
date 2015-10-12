package pjpl.s7.util;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * Wspiera wymianę danych z systemem opartym na formacie zapisu danych little endian
 * @author Piotr Janczura <piotr@janczura.pl>
 */
public class LittleEndianInArray{

	/**
	 * Jeżeli odczytany bajt != 0 zwracane jest true w innym przypadku false
	 * @param stream
	 * @return
	 * @throws IOException Puste wejście
	 */
	public static boolean _boolean(DataInputStream stream) throws IOException{
		return (boolean)( stream.readByte() > 0);
	}
	/**
	 * Jeżeli w buff na pozycji pos zapisana jest zmienna o wartości != 0 to zwracane jest true,
	 * w innym przypadku zwracane jest false
	 * @param buff Bufor na który zrzutowano zmienną.
	 * @param pos Pozycja pierwszego bajtu w buforze na który zrzutowano zmienną.
	 * @return
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public static boolean _boolean(byte[] buff, int pos) throws ArrayIndexOutOfBoundsException{
			return (boolean)( buff[pos] > 0);
	}
	/**
	 * Umieszcza wartość zmiennej logicznej val w buforze na pozycji pos.
	 * @param val
	 * @param buff
	 * @param pos
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public static void _boolean(boolean val, byte[] buff, int pos) throws ArrayIndexOutOfBoundsException{
			buff[pos] = (byte) ( val ? 1 : 0);
	}
	/**
	 * Pobiera wartość bitu posByte ze zmiennej zapisanej w buff na pozycji pos i zwraca ją jako boolean
	 * @param buff
	 * @param pos
	 * @param bit
	 * @return
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public static boolean _boolean(byte[] buff, int pos, int bit) throws ArrayIndexOutOfBoundsException{
		return  ( buff[pos] & Mask[bit] ) != 0 ;
//		if( pos < buff.length ){
//			if( bit < 0 ){ bit = 0; }; // @todo Może lepiej będzie rzucić wyjątek
//			if( bit > 7 ){ bit = 7; }; // @todo Może lepiej będzie rzucić wyjątek
//			return  ( buff[pos] & Mask[bit] ) != 0 ;
//		}else{
//			throw new ArrayIndexOutOfBoundsException();
//		}
	}
	public static void _boolean(boolean val, byte[] buff, int start, int pos){
		throw new UnsupportedOperationException("Not supported yet. :  MappingStreamVariable._boolean(boolean val, byte[] buff, int start, int pos)");
	}
	public static char _char(byte[] buff, int start){
		throw new UnsupportedOperationException("Not supported yet. :  MappingStreamVariable._char(byte[] buff, int start)");
	}
	public static void _char(char val, byte[] buff, int start){
		throw new UnsupportedOperationException("Not supported yet. :  MappingStreamVariable._char(char val, byte[] buff, int start)");
	}
	public static byte _byte(byte[] buff, int start){
		throw new UnsupportedOperationException("Not supported yet. :  MappingStreamVariable._byte(byte[] buff, int start)");
	}
	public static void _byte(byte val, byte[] buff, int start){
		throw new UnsupportedOperationException("Not supported yet. :  MappingStreamVariable._byte(byte val, byte[] buff, int start)");
	}
	public static short _short(byte[] buff, int start){
		throw new UnsupportedOperationException("Not supported yet. :  MappingStreamVariable._short(byte[] buff, int start)");
	}
	public static void _short(short val, byte[] buff, int start){
		throw new UnsupportedOperationException("Not supported yet. :  MappingStreamVariable._short(short val, byte[] buff, int start)");
	}
	/**
	 * Pobiera ze strumienia wartość zmiennej typu short
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static short _short(DataInputStream inputStream) throws IOException{
		byte lo = inputStream.readByte();
		byte hi = inputStream.readByte();
		return (short)( (( hi << 8 ) & 0x0000ff00 ) | ( lo & 0x000000ff ) );
	}
	public static int _int(byte[] buff, int start){
		throw new UnsupportedOperationException("Not supported yet. :  MappingStreamVariable._int(byte[] buff, int start)");
	}
	public static void _int(int val, byte[] buff, int start){
		throw new UnsupportedOperationException("Not supported yet. :  MappingStreamVariable._int(int val, byte[] buff, int start)");
	}
	public static long _long(byte[] buff, int start){
		throw new UnsupportedOperationException("Not supported yet. :  MappingStreamVariable._long(byte[] buff, int start)");
	}
	public static void _long(long val, byte[] buff, int start){
		throw new UnsupportedOperationException("Not supported yet. :  MappingStreamVariable._long(long val, byte[] buff, int start)");
	}
	/**
	 * Pobiera ze strumienia wartość zmiennej typy float
	 * @param stream
	 * @return
	 * @throws IOException
	 */
	public static float _float(DataInputStream stream) throws IOException{
		int i0 =    stream.readByte() & 0x000000ff ;
		int i1 = (( stream.readByte() & 0x000000ff ) <<  8  );
		int i2 = (( stream.readByte() & 0x000000ff ) << 16  );
		int i3 = (( stream.readByte() & 0x000000ff ) << 24  );

		return  Float.intBitsToFloat(( i3 | i2 | i1 | i0 )  );
	}
	public static float _float(byte[] buff, int start){
		int i0 =    buff[start] & 0x000000ff ;
		int i1 = (( buff[start + 1] & 0x000000ff ) <<  8  );
		int i2 = (( buff[start + 2] & 0x000000ff ) << 16  );
		int i3 = (( buff[start + 3] & 0x000000ff ) << 24  );

		return  Float.intBitsToFloat(( i3 | i2 | i1 | i0 )  );
	}
	public static void _float(float val, byte[] buff, int start){
		throw new UnsupportedOperationException("Not supported yet. :  MappingStreamVariable._float(float val, byte[] buff, int start)");
	}
	public static double _double(byte[] buff, int start){
		throw new UnsupportedOperationException("Not supported yet. :  MappingStreamVariable._double(byte[] buff, int start)");
	}
	public static void _double(double val, byte[] buff, int start){
		throw new UnsupportedOperationException("Not supported yet. :  MappingStreamVariable._double(double val, byte[] buff, int start)");
	}

	private static final byte[] Mask = {
			(byte)0x01,(byte)0x02,(byte)0x04,(byte)0x08,
			(byte)0x10,(byte)0x20,(byte)0x40,(byte)0x80
	};
}
