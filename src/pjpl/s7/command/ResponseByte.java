package pjpl.s7.command;

import java.io.IOException;
import java.net.Socket;
import pjpl.s7.common.ResponseCode;
import pjpl.s7.util.VariableInArray;

/**
 */
public class ResponseByte extends CommandResponse{

	public ResponseByte(byte processId, short commandCode, byte val, Socket socket) throws IOException {
		super(processId, commandCode, ResponseCode.RETURN_BYTE, socket);
		this.val = val;
	}

	public byte getValue(){
		return val;
	}

	@Override
	protected void prepareBuff() {
		VariableInArray._byte(getProcessId(), buff, 0);
		VariableInArray._short(getCommandCode(), buff, 1);
		VariableInArray._short(getResponseCode(), buff, 3);
		VariableInArray._byte(getValue(), buff, 5);
	}
	@Override
	protected void calculateBuffSize() {
		buffSize = 6;
	}

	private final byte val;
}
