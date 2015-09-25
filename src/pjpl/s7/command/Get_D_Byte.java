package pjpl.s7.command;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import pjpl.s7.common.CommandCode;

/**
 */
public class Get_D_Byte extends Command{

	public Get_D_Byte(byte processId, DataInputStream commandInputStream, OutputStream outputStream) throws IOException {
		super(processId, commandInputStream, outputStream);
	}

	@Override
	protected void loadParameters() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public CommandResponse action(pjpl.s7.process.Process process) {
		return new ResponseNo(processId, outputStream);
	}

	@Override
	public short getCommandCode() {
		return (short)CommandCode.GET_D_BYTE;
	}

}
