package pjpl.s7.command;

import java.io.IOException;
import java.net.Socket;
import pjpl.s7.util.VariableInArray;

public class ResponseInt extends CommandResponse{

	public ResponseInt(byte processId, short commandCode, short responseCode, short val, Socket socket) throws IOException {
		super(processId, commandCode, responseCode, socket);
		this.val = val;
	}
	public short getValue(){
		return val;
	}
	@Override
	protected void calculateBuffSize() {
		buffSize = 7;
	}

	@Override
	protected void prepareBuff() {
		VariableInArray._byte(getProcessId(), buff, 0);
		VariableInArray._short(getCommandCode(), buff, 1);
		VariableInArray._short(getResponseCode(), buff, 3);
		VariableInArray._short(getValue(), buff, 5);
	}

	private final short val;

}
