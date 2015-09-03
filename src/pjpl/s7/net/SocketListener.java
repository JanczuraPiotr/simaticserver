package pjpl.s7.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Piotr Janczura <piotr@janczura.pl>
 */
public class SocketListener extends Thread{

	public SocketListener() throws IOException {
		this.listener = new ServerSocket(9999);
	}
	@Override
	public void run(){
		while( doRun ){
			try {
				System.out.println("SocketListener.run : czekanie na gniazdko");
				socket = listener.accept();
				System.out.println("SocketListener.run : gniazdko otwarte");

			} catch (IOException ex) {
				Logger.getLogger(SocketListener.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
	private boolean doRun = true;
	private ServerSocket listener;
	private Socket socket;
}
