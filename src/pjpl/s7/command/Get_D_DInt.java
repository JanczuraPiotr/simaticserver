package pjpl.s7.command;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import pjpl.s7.common.CommandCode;

/**
 */
public class Get_D_DInt extends Command{

	public Get_D_DInt(byte processId, Socket socket) throws IOException {
		super(processId, socket);
	}

	@Override
	protected void loadParameters() {
		try {
			addr = dataInputStream.readUnsignedShort();
		} catch (IOException ex) {
			Logger.getLogger(Get_D_Byte.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	@Override
	public CommandResponse action(pjpl.s7.process.Process process) {
		try {
			val = process.getMemClip().memD.readDInt(addr);
			return new ResponseDInt(getProcessId(), getCommandCode(), val, socket);
		} catch (IOException ex) {
			Logger.getLogger(Get_D_DInt.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	@Override
	public short getCommandCode() {
		return (short)CommandCode.GET_D_DINT;
	}

	private int addr;
	private int val;
}
