package pjpl.s7.net;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import pjpl.s7.util.MappingLittleEndianVariable;

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
		int code = 0;
		float f = 0;

		while( doRun ){
			try {

				s = "";
				doRead = true;
				System.out.println("SocketListener.run : czekanie na gniazdko");
				socket = listener.accept();
				inputData =  new DataInputStream( socket.getInputStream() );
				System.out.println("SocketListener.run : gniazdko otwarte");

				code = MappingLittleEndianVariable._int(inputData);
				switch (code){
					
				}

				if( 4096 > code && code > -1 ){ // komendy wewnętrzne biblioteki pjpl.s7
					System.out.println("Kod wewnętrzny piblioteki : "+code);
				}else if( 4095 < code && code < 8192 ){
					System.out.println("Kod nieprzypisany : "+code);
				}else if( 8191 < code && code < 16384 ){
					System.out.println("Kod nie przypisany : "+code);
				}else if( 16383 < code && code < 32768 ){
					System.out.println("Kod nieprzypisany : "+code);
				}else if( 32767 < code && code < 65536 ){ // komendy aplikacji
					System.out.println("Kod specyficzny dla aplikacji : "+code);
				}else{
					System.out.println("Niespodziewany kod : "+code);
				}

				System.out.println(s);
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
