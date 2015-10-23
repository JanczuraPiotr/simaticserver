package pjpl.s7.net;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import static java.lang.Thread.sleep;
import java.net.Socket;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import pjpl.s7.command.Command;
import pjpl.s7.command.CommandBuilder;
import pjpl.s7.command.CommandWebListener;
import pjpl.s7.common.CommandCode;
import pjpl.s7.common.ConstProcess;
import pjpl.s7.util.ReadDataStream;

/**
 * @author Piotr Janczura <piotr@janczura.pl>
 */
public final class ScadaThread extends Thread{
	public ScadaThread( HashMap<Byte, pjpl.s7.process.Process> processes, Socket socket) throws IOException{
		this.processes = processes;
		this.socket = socket;
		this.doRun = true;
		this.is = this.socket.getInputStream();
		this.bis = new BufferedInputStream(is);
		this.isr = new InputStreamReader(is);
		this.br = new BufferedReader(isr);
		this.commandBuilder = new CommandBuilder();
		this.processesCommands = new Hashtable< >();
		processes.get(ConstProcess.PROCESS1_ID);
		this.addQueue(ConstProcess.PROCESS1_ID,processes.get(ConstProcess.PROCESS1_ID).getCommadQueue());
	}

	//------------------------------------------------------------------------------
	// interfejs

		public void addQueue( byte processId, Queue<pjpl.s7.command.Command> queue){
			processesCommands.put(processId, queue);
		}

	// interfejs
	//------------------------------------------------------------------------------

	public void run(){
		short commandCode;
		byte processId;
		String s = "";
		Command command = null;

		while(doRun){
			try {
				s = "";
				commandCode = CommandCode.NO;
				processId = (byte)255;

//				waitForSocket(CommandCode.MINIMAL_COMMAND_SIZE);
				commandCode = ReadDataStream.readShort(is);
				processId = ReadDataStream.readByte(is);

				s += String.format("commandCode = 0x%04X \n", commandCode );
				s += String.format("processId = 0x%04X \n", processId );

				switch(commandCode){
					// OK, true, YES
					case (short)0x0000:
						// @todo !!!!
						break;
					// NO, false
					case (short)0xFFFF:
						// @todo !!!!
						break;
					default:
						processesCommands
								.get(processId)
								.add(
										(Command)commandBuilder
												.build(
														commandCode
														, processId
														, socket
												)
								);
				}

			} catch (NullPointerException ex){
				System.err.println(String.format("NullPointerException"));
			} catch (IOException ex) {
				Logger.getLogger(ScadaThread.class.getName()).log(Level.SEVERE, null, ex);
			}finally{
				if(s.length() > 1){
					System.out.println(s);
				}
			}
		}
	}

	private void waitForSocket(short minimalCommandSize ){
		// @todo !!! MatkoBosko zrób coś z tym !!!
		long currentSleep = 0;
		System.out.println(String.format("sleepStart : %d",sleepStart));
		try {
			while ( is.available() < minimalCommandSize ) {
				try {
					sleep(sleepStart);
					currentSleep += sleepStart;
					if(sleepStart > 1 ){
						sleepStart -= sleepStart * 0.9;
						if(sleepStart < 1){
							sleepStart = 1;
						}
					}
				} catch (InterruptedException ex) {
					Logger.getLogger(CommandWebListener.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		} catch (IOException ex) {
			Logger.getLogger(CommandWebListener.class.getName()).log(Level.SEVERE, null, ex);
		}
		sleepStart = currentSleep - (long) (currentSleep * 0.1);
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
	private BufferedInputStream bis;
	private InputStreamReader isr;
	private BufferedReader br;
	private boolean doRun;
	private long sleepStart = 1;
	private final CommandBuilder commandBuilder;
	private final Hashtable<Byte, Queue<pjpl.s7.command.Command> > processesCommands;
	private static final int buffSize = 4096;
}
