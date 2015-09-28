package pjpl.s7.command;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import pjpl.s7.common.CommandCode;
import pjpl.s7.util.VariableInArray;

/**
 */
public class ResponseOk extends CommandResponse{

	public ResponseOk( byte processId, short commandCode, Socket socket) throws IOException {
		super(processId, commandCode, socket);
	}

	@Override
	public short getResponseCode() {
		return CommandCode.OK;
	}

	@Override
	public void sendToStream() {
		VariableInArray._byte(getProcessId(), buff, 0);
		VariableInArray._short(getCommandCode(), buff, 1);
		VariableInArray._short(getResponseCode(), buff, 3);
		try {
			outputStream.write(buff);
			outputStream.close();
		} catch (IOException ex) {
			Logger.getLogger(ResponseOk.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

}
