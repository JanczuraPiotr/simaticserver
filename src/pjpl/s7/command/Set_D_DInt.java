package pjpl.s7.command;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import pjpl.s7.common.CommandCode;

/**
 */
public class Set_D_DInt extends Command{

	public Set_D_DInt(byte processId, Socket socket) throws IOException {
		super(processId, socket);
	}

	@Override
	protected void loadParameters() {
		try {
			addr = dataInputStream.readUnsignedShort();
			val = dataInputStream.readShort();
		} catch (IOException ex) {
			Logger.getLogger(Set_D_DInt.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	@Override
	public CommandResponse action(pjpl.s7.process.Process process) {
		try {
			process.getMemClip().memD.write(addr, val);
			return new ResponseOk(getProcessId(), getCommandCode(), socket);
		} catch (IOException ex) {
			Logger.getLogger(Set_D_DInt.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	@Override
	public short getCommandCode() {
		return (short)CommandCode.SET_D_DINT;
	}

	private int addr;
	private short val;
}
