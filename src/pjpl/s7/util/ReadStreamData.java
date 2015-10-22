package pjpl.s7.util;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Piotr Janczura <piotr@janczura.pl>
 */
public class ReadStreamData {

	public byte readByte(InputStream is) throws IOException{
		int inputVar = is.read();
		if(inputVar < 0 ){
			throw new IOException("bład przy odczycie bajtu se strumienia");
		}
		return (byte)( inputVar & 0x000000FF) ;
	}

}
