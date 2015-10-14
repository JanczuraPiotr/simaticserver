package pjpl.s7.command;

import java.io.IOException;
import java.net.Socket;
import pjpl.s7.common.ResponseCode;
import pjpl.s7.util.BigEndianInArray;

public class ResponseDInt extends CommandResponse{

	public ResponseDInt(byte processId, short commandCode, int val, Socket socket) throws IOException {
		super(processId, commandCode, ResponseCode.RETURN_DINT, socket);
		this.val = val;
	}
	public int getValue(){
		return val;
	}
	@Override
	protected void calculateBuffSize() {
		buffSize = 9;
	}

	@Override
	protected void prepareBuff() {
		BigEndianInArray._byte(getProcessId(), buff, 0);
		BigEndianInArray._short(getCommandCode(), buff, 1);
		BigEndianInArray._short(getResponseCode(), buff, 3);
		BigEndianInArray._int(getValue(), buff, 5);
	}
	private final int val;
}
