package pjpl.s7.net;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author pjanczura
 */
public class SocketServerThread extends Thread{

	public SocketServerThread(int port, HashMap<Byte, pjpl.s7.process.Process> processes) throws IOException{
		this.processes = processes;
		this.port = port;
		this.doRun = true;
		this.serverSocket = new ServerSocket(port);
	}
	public void run(){
		ScadaThread tmpThread;
		while(doRun){
			try {
				Socket clientSocket = serverSocket.accept();
				tmpThread = new ScadaThread(processes, clientSocket);
				tmpThread.start();
			} catch (IOException ex) {
				Logger.getLogger(SocketServerThread.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	/**
	 * Gniazdo na którym server czaka na zgłoszenia od klientów SCADA
	 */
	private ServerSocket serverSocket;
	/**
	 * Lista procesów nadzorowanych przez server
	 */
	private HashMap<Byte, pjpl.s7.process.Process> processes;
	private boolean doRun;
	private int port;
}
