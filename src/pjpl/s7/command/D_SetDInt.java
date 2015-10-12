package pjpl.s7.command;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import pjpl.s7.common.CommandCode;

/**
 */
public class D_SetDInt extends Command{

	public D_SetDInt(byte processId, Socket socket) throws IOException {
		super(processId, socket);
	}

	@Override
	protected void loadParameters() {
		try {
			addr = dataInputStream.readUnsignedShort();
			val = dataInputStream.readShort();
		} catch (IOException ex) {
			Logger.getLogger(D_SetDInt.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	@Override
	public CommandResponse action(pjpl.s7.process.Process process) {
		try {
			process.getMemClip().memD.write(addr, val);
			return new ResponseOk(getProcessId(), getCommandCode(), socket);
		} catch (IOException ex) {
			Logger.getLogger(D_SetDInt.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	@Override
	public short getCommandCode() {
		return (short)CommandCode.D_SET_DINT;
	}

	private int addr;
	private short val;
}
