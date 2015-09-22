package pjpl.s7.command;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
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
		this.processesCommands = new Hashtable<Integer, Queue<pjpl.s7.command.Command> >();

		doRun = true;
	}

	//------------------------------------------------------------------------------
	// intefejs

		public void addQueue( int procesId, Queue<pjpl.s7.command.Command> queue){
			processesCommands.put(procesId, queue);
		}

	// intefejs
	//------------------------------------------------------------------------------

	@Override
	public void run(){
		Command command = null;
		String s = "";
		// kod komendy
		int code = 0;
		// aresat komendy 0xFFFF - bez adresu
		int addr = 0;
		float f = 0;

		while( doRun ){
			try {

				s = "";
				doRead = true;
				System.out.println("CommandWebListener.run : czekanie na gniazdko");
				socket = listener.accept();
				inputData =  new DataInputStream( socket.getInputStream() );
				System.out.println("CommandWebListener.run : gniazdko otwarte");

				code = MappingLittleEndianVariable._int(inputData);
				addr = MappingLittleEndianVariable._int(inputData);
				System.out.println("code = "+code+" addr = "+addr);

				switch(code){
					// OK, true, YES
					case 0x0000:
						// @todo !!!!
						break;
					// NO, false
					case 0xFFFF:
						// @todo !!!!
						break;
					default:
						processesCommands.get(addr).add(commandBuilder.build(code, inputData));
				}
				System.out.println(s);
			} catch (IOException ex) {
				Logger.getLogger(CommandWebListener.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
	private boolean doRun = true;
	private boolean doRead = true;

	private final ServerSocket listener;
	private DataInputStream inputData;
	private Socket socket;
	private final CommandBuilder commandBuilder;
//	private final Queue<pjpl.s7.command.Command> commandQueue;
	private final Hashtable<Integer, Queue<pjpl.s7.command.Command> > processesCommands;

}
