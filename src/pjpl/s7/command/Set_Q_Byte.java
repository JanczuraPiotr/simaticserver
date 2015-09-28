package pjpl.s7.command;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import pjpl.s7.common.CommandCode;

/**
 */
public class Set_Q_Byte extends Command{

	public Set_Q_Byte(byte processId, Socket socket) throws IOException {
		super(processId, socket);
	}

	@Override
	protected void loadParameters() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public CommandResponse action(pjpl.s7.process.Process process) {
		try {
			return new ResponseNo(processId, getCommandCode(), socket);
		} catch (IOException ex) {
			Logger.getLogger(Set_Q_Byte.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	@Override
	public short getCommandCode() {
		return (short)CommandCode.SET_Q_BYTE;
	}

}
