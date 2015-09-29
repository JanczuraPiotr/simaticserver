package pjpl.s7.command;

import java.io.IOException;
import java.net.Socket;
import pjpl.s7.util.VariableInArray;

public class ResponseReal extends CommandResponse{

	public ResponseReal(byte processId, short commandCode, short responseCode, float val, Socket socket) throws IOException {
		super(processId, commandCode, responseCode, socket);
		this.val = val;
	}

	public float getValue(){
		return val;
	}
	@Override
	protected void calculateBuffSize() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	protected void prepareBuff() {
		VariableInArray._byte(getProcessId(), buff, 0);
		VariableInArray._short(getCommandCode(), buff, 1);
		VariableInArray._short(getResponseCode(), buff, 3);
		VariableInArray._float(getValue(), buff, 5);
	}

	private final float val;
}
