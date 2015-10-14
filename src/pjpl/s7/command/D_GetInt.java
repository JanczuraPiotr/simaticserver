package pjpl.s7.command;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import pjpl.s7.common.CommandCode;

/**
 */
public class D_GetInt extends Command{

	public D_GetInt(byte processId, Socket socket) throws IOException {
		super(processId, socket);
	}

	@Override
	protected void loadParameters() {
		try {
			addr = dataInputStream.readUnsignedShort();
		} catch (IOException ex) {
			Logger.getLogger(D_GetByte.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	@Override
	public CommandResponse action(pjpl.s7.process.Process process) {
		try {
			val = process.getMemClip().memD.readInt(addr);
			System.out.println(String.format("wartość komórki o kodzie 0x%02X = 0x%04X", addr, val));
			return new ResponseInt(getProcessId(), getCommandCode(), val, socket);
		} catch (IOException ex) {
			Logger.getLogger(D_GetInt.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	@Override
	public short getCommandCode() {
		return (short)CommandCode.D_GET_INT;
	}

	private int addr;
	private short val;

}
