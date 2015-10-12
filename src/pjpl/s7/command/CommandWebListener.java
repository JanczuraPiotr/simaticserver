package pjpl.s7.command;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

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
			try {
				doRead = true;
				socket = listener.accept();
				inputSocketStream = socket.getInputStream();
				outputSocketStream = socket.getOutputStream();
				inputData =  new DataInputStream( inputSocketStream );
				outputData = new DataOutputStream(outputSocketStream);
				socketPrintWriter = new PrintWriter(socket.getOutputStream(), true);

				commandCode = inputData.readShort();
				processId = inputData.readByte();

				s += "commandCode = " + String.format("hex : %04X ", commandCode ) + "\n";
				s += "commandAddr = " + String.format("hex : %04X ", processId ) + "\n";

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
				System.out.println(s);
			}
		}
	}
	private boolean doRun = true;
	private boolean doRead = true;

	private final ServerSocket listener;
	private InputStream inputSocketStream;
	private OutputStream outputSocketStream;
	private DataInputStream inputData;
	private DataOutputStream outputData;
	private PrintWriter socketPrintWriter;
	private Socket socket;
	private final CommandBuilder commandBuilder;
	private final Hashtable<Byte, Queue<pjpl.s7.command.Command> > processesCommands;

}
