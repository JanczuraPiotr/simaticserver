package pjpl.s7.command;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * @author Piotr Janczura <piotr@janczura.pl>
 */
abstract public class Command { // _cmd


	public Command(DataInputStream commandInputStream) throws IOException{
		this.commandInputStream = commandInputStream;
		this.processId = this.commandInputStream.readByte();
	}
	public byte getProcesId(){
		return (byte) processId;
	}

	protected abstract void prepareContent();

	//------------------------------------------------------------------------------
	
	/**
	 * Strumień w którym znajdują się elementy komendy
	 */
	protected final DataInputStream commandInputStream;
	/**
	 * Identyfikator procesu, który powinien wykonać komendę
	 * Wartość zmiennej przekazywana jest w parametrach komendy w buforze za kodem Komendy.
	 */
	protected int processId;

}
