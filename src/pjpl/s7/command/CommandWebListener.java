package pjpl.s7.command;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import pjpl.s7.common.CommandCode;

/**
 * Nasłuchuje komend do wykonania na procesie.
 * Jedynym źródłem komend jest sieć.
 * @author Piotr Janczura <piotr@janczura.pl>
 * @todo Przerobić by każdy process miał własną kolejkę a CommandListener umieszczał komendy we właściwych kolejkach
 */
public class CommandWebListener extends Thread{

	public CommandWebListener(int port) throws IOException{
		this.listener = new ServerSocket(port);
		this.commandBuilder = new CommandBuilder();
		this.processesCommands = new Hashtable< >();

		doRun = true;
	}

	//------------------------------------------------------------------------------
	// interfejs

		public void addQueue( byte processId, Queue<pjpl.s7.command.Command> queue){
			processesCommands.put(processId, queue);
		}

	// interfejs
	//------------------------------------------------------------------------------

	@Override
	public void run(){
		short commandCode;
		byte processId;
		Command command = null;

		String s = "";

		while( doRun ){
			s = "";
			try {
				doRead = true;
				socket = listener.accept();
				inputSocketStream = socket.getInputStream();
				outputSocketStream = socket.getOutputStream();
				inputData =  new DataInputStream( inputSocketStream );
				outputData = new DataOutputStream(outputSocketStream);

				waitForSocket(CommandCode.MINIMAL_COMMAND_SIZE);
				commandCode = inputData.readShort();
				processId = inputData.readByte();

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
			} catch (NullPointerException | IOException ex) {
				Logger.getLogger(CommandWebListener.class.getName()).log(Level.SEVERE, null, ex);
			}finally{
				System.out.println(s);
			}
		}
	}
	private void waitForSocket(short minimalCommandSize ){
		// @todo !!! MatkoBosko zrób coś z tym !!!
		long currentSleep = 0;
		System.out.println(String.format("sleepStart : %d",sleepStart));
		try {
			while ( inputData.available() < minimalCommandSize ) {
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

	private boolean doRun = true;
	private boolean doRead = true;
	private long sleepStart = 1;

	private final ServerSocket listener;
	private Socket socket;
	private InputStream inputSocketStream;
	private OutputStream outputSocketStream;
	private DataInputStream inputData;
	private DataOutputStream outputData;
	private final CommandBuilder commandBuilder;
	private final Hashtable<Byte, Queue<pjpl.s7.command.Command> > processesCommands;

}
