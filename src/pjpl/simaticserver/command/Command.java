package pjpl.simaticserver.command;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * @author Piotr Janczura <piotr@janczura.pl>
 */
public abstract class Command {
	public Command(DataInputStream commandInputStream) throws IOException{
		this.commandInputStream = commandInputStream;
		this.processId = this.commandInputStream.readByte();
		this.deviceId = this.commandInputStream.readByte();
	}
	public byte getProcesId(){
		return (byte) processId;
	}
	public byte getDeviceId(){
		return (byte) deviceId;
	}
	protected abstract void prepareWariable();
	protected final DataInputStream commandInputStream;
	// identyfikator sterowanika
	protected int deviceId;
	protected int processId;
}
