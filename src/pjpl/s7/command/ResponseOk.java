package pjpl.s7.command;

import java.io.IOException;
import java.net.Socket;
import pjpl.s7.common.ResponseCode;
import pjpl.s7.common.ResponseGeneral;

/**
 * Odpowiedź zwracana gdy komenda zakończyła się powodzeniem a komenda nie żądała szczegółowej odpowiedzi lub gdy
 * komenda żądała odpowiedzi w formie TAK lub NIE.
 */
public class ResponseOk extends ResponseGeneral{

	public ResponseOk(byte processId, short commandCode, Socket socket) throws IOException {
		super(processId, commandCode, ResponseCode.OK, socket);
	}

}
