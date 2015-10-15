package pjpl.s7.command;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import pjpl.s7.common.CommandCode;
import pjpl.s7.util.BigEndianInArray;

/**
 */
public class D_SetInt extends Command{

	public D_SetInt(byte processId, Socket socket) throws IOException {
		super(processId, socket);
	}

	@Override
	protected void loadParameters() {

		try {
			varCode = dataInputStream.readUnsignedShort();
			varVal = dataInputStream.readShort();

		} catch (IOException ex) {
			Logger.getLogger(D_SetByte.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public CommandResponse action(pjpl.s7.process.Process process) {
		try {
			process.getMemClip().memD.write(varCode, varVal);
			return new ResponseOk(getProcessId(), getCommandCode(), socket);
		} catch (IOException ex) {
			Logger.getLogger(D_SetInt.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	@Override
	public short getCommandCode() {
		return (short)CommandCode.D_SET_INT;
	}

	private int varCode;
	private short varVal;

}
