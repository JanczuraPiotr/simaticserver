package pjpl.s7.command;

import Moka7.S7;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.layout.Priority;
import pjpl.s7.util.MappingLittleEndianVariable;

/**
 * Nasłuchuje komend do wykonania na procesie.
 * Jedynym źródłem komend jest sieć.
 * @author Piotr Janczura <piotr@janczura.pl>
 * @todo Przerobić by każdy process miał własną kolejkę a CommandListener umieszczał commendy we właściwych kolejkach
 */
public class CommandWebListener extends Thread{

	public CommandWebListener(int port) throws IOException{
		this.listener = new ServerSocket(port);
		this.commandBuilder = new CommandBuilder();
		this.processesCommands = new Hashtable<Byte, PriorityQueue<pjpl.s7.command.Command> >();

		doRun = true;
	}

	//------------------------------------------------------------------------------
	// intefejs

		public void addQueue( byte processId, PriorityQueue<pjpl.s7.command.Command> queue){
			System.out.println("CommandWebListener.addQueue");
			System.out.println("processId = "+processId);

			processesCommands.put(processId, queue);
		}

	// intefejs
	//------------------------------------------------------------------------------

	@Override
	public void run(){
		short commandCode = 0;
		byte commandAddr = 0;
		Command command = null;

		String s = "";
		// kod komendy
		int code = 0;
		// aresat komendy 0xFFFF - bez adresu
		int addr = 0;
		float f = 0;

		while( doRun ){
			try {
				doRead = true;
				System.out.println("CommandWebListener.run : czekanie na gniazdko");
				socket = listener.accept();
				socketStream = socket.getInputStream();
				inputData =  new DataInputStream( socketStream );
				System.out.println("CommandWebListener.run : gniazdko otwarte");

				commandCode = inputData.readShort();
				commandAddr = inputData.readByte();

				System.out.println("commandCode = " + String.format("hex : %04X dec   = %d",commandCode, commandCode ));
				System.out.println("commandAddr = " + String.format("hex : %04X dec   = %d",commandAddr, commandAddr ));

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
						// @prace 00 wyjątek podczas dodawania kolejnej komendy
						PriorityQueue<Command> tmpQ = processesCommands.get(commandAddr);
						command = commandBuilder.build(commandCode, commandAddr, inputData);
						System.out.println("processId = " + command.getProcesId());
						tmpQ.add(command);
//						processesCommands.get(commandAddr).add((Command)command);
						System.out.println("processCommand.queue.size = "+processesCommands.get(commandAddr).size());
//						System.out.println("processCommand.queue.peek.pricessId = "+processesCommands.get(commandAddr).peek().getProcesId());
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
	private InputStream socketStream;
	private DataInputStream inputData;
	private Socket socket;
	private final CommandBuilder commandBuilder;
//	private final Queue<pjpl.s7.command.Command> commandQueue;
	private final Hashtable<Byte, PriorityQueue<pjpl.s7.command.Command> > processesCommands;

}
