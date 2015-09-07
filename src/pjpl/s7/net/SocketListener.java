package pjpl.s7.net;

import java.io.DataInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Piotr Janczura <piotr@janczura.pl>
 */
public class SocketListener extends Thread{

	public SocketListener() throws IOException {
		this.listener = new ServerSocket(9999);
		doRun = true;
	}
	@Override
	public void run(){
		String s = "";
		byte codeLo = 0;
		byte codeHi = 0;
		int code = 0;
		float f = 0;
		byte b, b0,b1,b2,b3;
		int  i, i0,i1,i2,i3;
		double d = 0;

		while( doRun ){
			try {

				s = "";
				doRead = true;
				System.out.println("SocketListener.run : czekanie na gniazdko");
				socket = listener.accept();
				inputData =  new DataInputStream( socket.getInputStream() );
				System.out.println("SocketListener.run : gniazdko otwarte");

				codeLo = inputData.readByte();
				codeHi = inputData.readByte();
				code = ( (( codeHi << 8 ) & 0x0000ff00 ) | ( codeLo & 0x000000ff ) );

				b0 = inputData.readByte();
				b1 = inputData.readByte();
				b2 = inputData.readByte();
				b3 = inputData.readByte();

				i0 = b0;
				i1 = (( b1 & 0x000000ff ) <<  8  );
				i2 = (( b2 & 0x000000ff ) << 16  );
				i3 = (( b3 & 0x000000ff ) << 24  );

				i = ( i0 | i1 | i2 | i3 );
				f = Float.intBitsToFloat(i);

				System.out.println("code  : "+code);
				System.out.println("pomiar = " + f);

//				f =  ( f0  );
//				f =  ( (int)f | ( ( ( f1 & 0x000000ff ) << 8  ) ) );
//				f =  ( (int)f | ( ( ( f2 & 0x000000ff ) << 16 ) ) );
//				f =  ( (int)f | ( ( ( f3 & 0x000000ff ) << 24 ) ) );

//				f = (
//							    ( f0 & 0x000000FF )
//							| ( ( f1 & 0x000000FF ) << 8  )
//							| ( ( f2 & 0x000000FF ) << 16 )
//							| ( ( f3 & 0x000000FF ) << 24 )
//						);


//				if( 4096 > code && code > -1){ // komendy wewnętrzne biblioteki pjpl.s7
//					System.out.println("Kod wewnętrzny piblioteki : "+code);
//				}else if( 4095 < code && code < 8192 ){
//					System.out.println("Kod nie przypisany : "+code);
//				}else if( 8191 < code && code < 16384 ){
//					System.out.println("Kod nie przypisany : "+code);
//				}else if( 16383 < code && code < 32768 ){
//					System.out.println("Kod nie przypisany : "+code);
//				}else if( 32767 < code && code < 65536 ){
//					System.out.println("Kod specyficzny dla aplikacji : "+code);
//				}else{
//					System.out.println("Niespodziewany kod : "+code);
//				}
//				System.out.println(s);

			} catch (IOException ex) {
				Logger.getLogger(SocketListener.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
	private boolean doRun = true;
	private boolean doRead = true;
	private final ServerSocket listener;
	private DataInputStream inputData;
	private Socket socket;

}
