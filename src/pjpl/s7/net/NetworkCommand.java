package pjpl.s7.net;

import java.net.Socket;
import pjpl.s7.command.BuilderProcessCommand;

/**
 * Obróbka komendy nadesłanej przez sieć TCP.
 * Obiekt powinien być utworzony przez obiekt klasy SocketListener po odebraniu
 *
 * @author Piotr Janczura <piotr@janczura.pl>
 */
public class NetworkCommand extends Thread{
	/**
	 *
	 * @param socket Gniazdo utworzone do pobrania komendy
	 * @param process Process do którego należy przekazać utworzoną komendę
	 */
	public NetworkCommand(Socket socket, pjpl.s7.process.Process process){
		this.socket = socket;
		this.process = process;
		builderCommand = new BuilderProcessCommand();
	}
	@Override
	public void run(){

	}
	private final Socket socket;
	private final pjpl.s7.process.Process process;
	private final BuilderProcessCommand builderCommand;
}
/**
 * @prace 11 Utworzyć BuilderCommand i utworzoną za jego pomocą komendę którą przekazać do procesu.
 */