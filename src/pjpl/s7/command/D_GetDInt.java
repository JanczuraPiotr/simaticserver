package pjpl.s7.command;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import pjpl.s7.common.CommandCode;

/**
 */
public class D_GetDInt extends Command{

	public D_GetDInt(byte processId, Socket socket) throws IOException {
		super(processId, socket);
	}

	@Override
	protected void loadParameters() {
		try {
			varCode = dataInputStream.readUnsignedShort();
		} catch (IOException ex) {
			Logger.getLogger(D_GetByte.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	@Override
	public CommandResponse action(pjpl.s7.process.Process process) {
		try {
			varVal = process.getMemClip().memD.readDInt(varCode);
			return new ResponseDInt(getProcessId(), getCommandCode(), varVal, socket);
		} catch (IOException ex) {
			Logger.getLogger(D_GetDInt.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	@Override
	public short getCommandCode() {
		return (short)CommandCode.D_GET_DINT;
	}

	private int varCode;
	private int varVal;
}
