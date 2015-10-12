package pjpl.s7.command;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import pjpl.s7.common.CommandCode;

/**
 */
public class I_GetByte extends Command{

	public I_GetByte(byte processId, Socket socket) throws IOException {
		super(processId, socket);
	}

	@Override
	public CommandResponse action(pjpl.s7.process.Process process) {
		try {
			val = process.getMemClip().memI.readByte(addr);
			return new ResponseByte(getProcessId(), getCommandCode(), val, socket);
		} catch (IOException ex) {
			Logger.getLogger(I_GetByte.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	@Override
	protected void loadParameters() {
		try {
			addr = dataInputStream.readUnsignedShort();
		} catch (IOException ex) {
			Logger.getLogger(D_SetByte.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public short getCommandCode() {
		return (short)CommandCode.I_GET_BYTE;
	}

	//------------------------------------------------------------------------------
	private int addr;
	private byte val;
}
