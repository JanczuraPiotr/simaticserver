package pjpl.s7.command;

import java.io.IOException;
import java.net.Socket;
import pjpl.s7.common.ResponseCode;
import pjpl.s7.common.ResponseGeneral;

/**
 * Odpowiedź zwracana gdy komenda zakończyła się niepowodzeniem lub gdy komenda była zapytaniem na które odpowiedzią
 * może być TAK lub NIE.
 */
public class ResponseNo extends ResponseGeneral{

	public ResponseNo(byte processId, short commandCode, Socket socket) throws IOException {
		super(processId, commandCode, ResponseCode.NO, socket);
	}

}
