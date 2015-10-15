package pjpl.s7.command;

import java.io.IOException;
import java.net.Socket;
import pjpl.s7.common.ResponseCode;
import pjpl.s7.util.BigEndianInArray;

public class ResponseReal extends CommandResponse{

	public ResponseReal(byte processId, short commandCode, float varVal, Socket socket) throws IOException {
		super(processId, commandCode, ResponseCode.RETURN_REAL, socket);
		this.varVal = varVal;
	}

	public float getValue(){
		return varVal;
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
		BigEndianInArray._float(getValue(), buff, 5);
	}

	private final float varVal;
}
