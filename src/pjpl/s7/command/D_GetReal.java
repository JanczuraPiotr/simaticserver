package pjpl.s7.command;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import pjpl.s7.common.CommandCode;

/**
 */
public class D_GetReal extends Command{

	public D_GetReal(byte processId, Socket socket) throws IOException {
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

	public float getVarVal(){
		return varVal;
	}

	@Override
	public CommandResponse action(pjpl.s7.process.Process process) {
		try {

			byte[] mem = process.getMemClip().memD.getMem();

			for(int i = 0 ; i < mem.length; i++){
				System.out.println(String.format("buff[%d] = 0x%02X",i, mem[i]));
			}

			varVal = process.getMemClip().memD.readReal(varCode);
			return new ResponseReal(getProcessId(), getCommandCode(), varVal, socket);
		} catch (IOException ex) {
			Logger.getLogger(D_GetReal.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	@Override
	public short getCommandCode() {
		return (short)CommandCode.D_GET_REAL;
	}

	private int varCode;
	private float varVal;
}
