package pjpl.s7.common;

import java.io.IOException;
import java.net.Socket;
import pjpl.s7.command.CommandResponse;
import pjpl.s7.util.BigEndianInArray;

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
		BigEndianInArray._byte(getProcessId(), buff, 0);
		BigEndianInArray._short(getCommandCode(), buff, 1);
		BigEndianInArray._short(getResponseCode(), buff, 3);
	}

}
