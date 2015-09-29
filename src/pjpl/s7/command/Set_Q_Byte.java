package pjpl.s7.command;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import pjpl.s7.common.CommandCode;
import pjpl.s7.util.MemClip;

/**
 */
public class Set_Q_Byte extends Command{

	public Set_Q_Byte(byte processId, Socket socket) throws IOException {
		super(processId, socket);
	}

	@Override
	protected void loadParameters() {
		try {
			addr = dataInputStream.readUnsignedShort();
			val = dataInputStream.readByte();
		} catch (IOException ex) {
			Logger.getLogger(Set_Q_Byte.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public CommandResponse action(pjpl.s7.process.Process process) {
		try {
			MemClip memClip = process.getMemClip();
			memClip.memQ.write(addr,val);

			return new ResponseNo(getProcessId(), getCommandCode(), socket);
		} catch (IOException ex) {
			Logger.getLogger(Set_Q_Byte.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	@Override
	public short getCommandCode() {
		return (short)CommandCode.SET_Q_BYTE;
	}

	private int addr;
	private byte val;

}
