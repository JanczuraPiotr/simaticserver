package pjpl.s7.command;

import java.io.IOException;
import java.net.Socket;
import pjpl.s7.common.ResponseCode;
import pjpl.s7.util.BigEndianInArray;
import pjpl.s7.util.MemByteClip;

/**
 *
 * @author pjanczura
 */
public class ResponseRaportFull extends CommandResponse{

	public ResponseRaportFull(byte processId, short commandCode, MemByteClip raport, Socket socket) throws IOException {
		super(processId, commandCode, ResponseCode.RAPORT_FULL, socket);
		this.raport = raport;
	}

	@Override
	protected void calculateBuffSize() {
		buffSize = NAGLOWEK + raport.buffD.length + raport.buffI.length + raport.buffQ.length;
	}

	@Override
	protected void prepareBuff() {
		BigEndianInArray._byte(getProcessId(), buff, 0);
		BigEndianInArray._short(getCommandCode(), buff, 1);
		BigEndianInArray._short(getResponseCode(), buff, 3);
		BigEndianInArray._int(raport.buffD.length, buff, 5);
		BigEndianInArray._int(raport.buffI.length, buff, 9);
		BigEndianInArray._int(raport.buffQ.length, buff, 13);
		System.arraycopy(raport.buffD, 0, buff, 17,  raport.buffD.length);
		System.arraycopy(raport.buffI, 0, buff, 17 + raport.buffD.length, raport.buffI.length);
		System.arraycopy(raport.buffQ, 0, buff, 17 + raport.buffD.length + raport.buffI.length, raport.buffQ.length);

	}

	private MemByteClip raport;
	private static final int SYGNATURA = 5;
	private static final int WYMIARY = 12; // rozmiary buforów
	private static final int NAGLOWEK = SYGNATURA + WYMIARY;
}
