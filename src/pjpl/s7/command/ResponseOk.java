package pjpl.s7.command;

import java.io.OutputStream;
import pjpl.s7.common.CommandCode;

/**
 */
public class ResponseOk extends CommandResponse{

	public ResponseOk(short commandCode, OutputStream outputStream) {
		super(commandCode, outputStream);
	}

	@Override
	public short getResponseCode() {
		return CommandCode.OK;
	}

}
