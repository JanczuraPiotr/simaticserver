package pjpl.s7.command;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import pjpl.s7.common.CommandCode;

/**
 */
public class Get_I_Byte extends Command{

	public Get_I_Byte(byte processId, Socket socket) throws IOException {
		super(processId, socket);
	}

	@Override
	public CommandResponse action(pjpl.s7.process.Process process) {
		try {
			return new ResponseNo(getProcessId(), getCommandCode(), socket);
		} catch (IOException ex) {
			Logger.getLogger(Get_I_Byte.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	@Override
	protected void loadParameters() {
	}

	@Override
	public short getCommandCode() {
		return (short)CommandCode.GET_I_BYTE;
	}

}
