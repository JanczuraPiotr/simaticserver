package pjpl.s7.command;

import java.io.IOException;
import java.net.Socket;
import pjpl.s7.util.BigEndianInArray;

public class ResponseBuff extends CommandResponse{

	public ResponseBuff(byte processId, short commandCode, short responseCode, byte[] buff, int buffSize, Socket socket) throws IOException {
		super(processId, commandCode, responseCode, socket);
		this.inBuff = buff;
		this.inBuffSize = buffSize;
	}

	@Override
	protected void calculateBuffSize() {
		buffSize = inBuffSize + 7; // 7 = 5(rozmiar atrybutów) + 2(rozmiar inBuffSize) bo rozmiar jest wysyłany jako short
	}

	@Override
	protected void prepareBuff() {
		BigEndianInArray._byte(getProcessId(), buff, 0);
		BigEndianInArray._short(getCommandCode(), buff, 1);
		BigEndianInArray._short(getResponseCode(), buff, 3);
		BigEndianInArray._short((short)inBuffSize, buff, 5);
		System.arraycopy(inBuff, 0, buff, 7, inBuffSize);
	}

	private final byte[] inBuff;
	private final int inBuffSize;
}
