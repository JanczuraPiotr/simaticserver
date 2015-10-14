package pjpl.s7.command;

import java.io.IOException;
import java.net.Socket;
import pjpl.s7.common.ResponseCode;
import pjpl.s7.util.BigEndianInArray;

public class ResponseInt extends CommandResponse{

	public ResponseInt(byte processId, short commandCode, short val, Socket socket) throws IOException {
		super(processId, commandCode, ResponseCode.RETURN_INT, socket);
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
		System.out.println(String.format("commandCode : 0x%04X", getCommandCode()));
		System.out.println(String.format("responseCode : 0x%04X", getResponseCode()));
		System.out.println(String.format("val : 0x%04X", getValue()));
	}

	private final short val;

}
