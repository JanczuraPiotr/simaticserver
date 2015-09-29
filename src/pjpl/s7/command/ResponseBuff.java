package pjpl.s7.command;

import java.io.IOException;
import java.net.Socket;

public class ResponseBuff extends CommandResponse{

	public ResponseBuff(byte processId, short commandCode, short responseCode, byte[] buff, int buffSize, Socket socket) throws IOException {
		super(processId, commandCode, responseCode, socket);
		this.inBuff = buff;
		this.inBuffSize = buffSize;
	}

	@Override
	protected void calculateBuffSize() {
		buffSize = inBuffSize + 9; // 6 = rozmiar atrybutów + rozmiar inBuffSize
	}

	@Override
	protected void prepareBuff() {
		// @prace 00 !!
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	private final byte[] inBuff;
	private final int inBuffSize;
}
