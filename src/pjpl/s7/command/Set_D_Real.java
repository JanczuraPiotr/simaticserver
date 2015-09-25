package pjpl.s7.command;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import pjpl.s7.common.CommandCode;
import pjpl.s7.util.MemClip;

/**
 */
public class Set_D_Real extends Command{

	public Set_D_Real(byte processId, DataInputStream commandInputStream, OutputStream outputStream) throws IOException {
		super(processId, commandInputStream, outputStream);
	}

	@Override
	protected void loadParameters() {
	}

	@Override
	public CommandResponse action(pjpl.s7.process.Process process) {
		return new ResponseNo(processId, outputStream);
	}

	@Override
	public short getCommandCode() {
		return (short)CommandCode.SET_D_REAL;
	}

}
