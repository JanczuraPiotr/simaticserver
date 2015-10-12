package pjpl.s7.command;

import java.io.IOException;
import java.net.Socket;
import pjpl.s7.common.ResponseCode;
import pjpl.s7.util.BigEndianInArray;

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
		BigEndianInArray._byte(getProcessId(), buff, 0);
		BigEndianInArray._short(getCommandCode(), buff, 1);
		BigEndianInArray._short(getResponseCode(), buff, 3);
		BigEndianInArray._byte(getValue(), buff, 5);
	}
	@Override
	protected void calculateBuffSize() {
		buffSize = 6;
	}

	private final byte val;
}
