package pjpl.s7.command;

import java.io.IOException;
import java.net.Socket;
import pjpl.s7.util.VariableInArray;

public class ResponseDInt extends CommandResponse{

	public ResponseDInt(byte processId, short commandCode, short responseCode, int val, Socket socket) throws IOException {
		super(processId, commandCode, responseCode, socket);
		this.val = val;
	}
	public int getValue(){
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
		VariableInArray._int(getValue(), buff, 5);
	}
	private final int val;
}
