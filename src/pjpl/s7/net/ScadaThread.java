package pjpl.s7.net;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Piotr Janczura <piotr@janczura.pl>
 */
public class ScadaThread extends Thread{
	public ScadaThread( HashMap<Byte, pjpl.s7.process.Process> processes, Socket socket) throws IOException{
		this.processes = processes;
		this.socket = socket;
		this.doRun = true;
		this.is = this.socket.getInputStream();

	}

	public void run(){
		int countRead = 0;
		int countComplited = 0;
		byte[] buff = new byte[buffSize];
		short commandCode;
		byte processId;
		String s = "";
		int inputVar;

		while(doRun){
			try {
				s = "";
//				commandCode = inputData.readShort();
//				processId = inputData.readByte();
//				buff[2] = (byte)is.read();
//				s += String.format("buff[0] = 0x%02X \n", buff[0] );
//				s += String.format("buff[1] = 0x%02X \n", buff[1] );


				while ( ( inputVar = is.read() ) >= 0 ){
					s += String.format(" inputVar = 0x%2X \n", (inputVar & 0x000000FF) );
				}

			} catch (IOException ex) {
				Logger.getLogger(ScadaThread.class.getName()).log(Level.SEVERE, null, ex);
			}finally{
				if(s.length() > 1){
					System.out.println(s);
				}
			}
		}
	}

	/**
	 * Process dla którego powstał ten wątek
	 */
	private HashMap<Byte, pjpl.s7.process.Process> processes;
	/**
	 * Gniazdo do scada nadzorującej proces
	 */
	private Socket socket;
	/**
	 * Koleka komend nadsyłanych do procesu.
	 * Proces obsługuje te komendy oraz odpowiedzi wygenerowane w rezultacie wykonania komendy w swoim wątku.
	 */
	private Queue<pjpl.s7.command.Command> commandQueue;
	private InputStream is;
	private boolean doRun;

	private static final int buffSize = 4096;
}
