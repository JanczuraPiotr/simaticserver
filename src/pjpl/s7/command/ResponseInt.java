package pjpl.s7.command;

import java.io.IOException;
import java.net.Socket;
import pjpl.s7.common.ResponseCode;
import pjpl.s7.util.BigEndianInArray;

public class ResponseInt extends CommandResponse{

	public ResponseInt(byte processId, short commandCode,short val, Socket socket) throws IOException {
		super(processId, commandCode, ResponseCode.D_GET_INT, socket);
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
		BigEndianInArray._byte(getProcessId(), buff, 0);
		BigEndianInArray._short(getCommandCode(), buff, 1);
		BigEndianInArray._short(getResponseCode(), buff, 3);
		BigEndianInArray._short(getValue(), buff, 5);
	}

	private final short val;

}
