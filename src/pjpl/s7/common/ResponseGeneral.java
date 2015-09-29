package pjpl.s7.common;

import java.io.IOException;
import java.net.Socket;
import pjpl.s7.command.CommandResponse;
import pjpl.s7.util.VariableInArray;

/**
 * Domyślna klasa do generowania odpowiedzi.
 */
public class ResponseGeneral extends CommandResponse{

	public ResponseGeneral(byte processId, short commandCode, short responseCode, Socket socket) throws IOException {
		super(processId, commandCode, responseCode, socket);
	}
	@Override
	protected void calculateBuffSize() {
		buffSize = 5;
	}
	@Override
	protected void prepareBuff() {
		VariableInArray._byte(getProcessId(), buff, 0);
		VariableInArray._short(getCommandCode(), buff, 1);
		VariableInArray._short(getResponseCode(), buff, 3);
	}

}
