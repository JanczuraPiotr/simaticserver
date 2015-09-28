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
			addrQ = dataInputStream.readShort();
			valQ = dataInputStream.readByte();
			System.out.printf("commandCode = %04X \n", getCommandCode());
			System.out.printf("processId = %04X \n", getProcesId());
			System.out.printf("addrQ = %02X \n", addrQ);
			System.out.printf("valQ = %02X \n", valQ);
		} catch (IOException ex) {
			Logger.getLogger(Set_Q_Byte.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public CommandResponse action(pjpl.s7.process.Process process) {
		try {
			MemClip memClip = process.getMemClip();
			memClip.memQ.write(addrQ,valQ);

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

	private int addrQ;
	private byte valQ;

}
