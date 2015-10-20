package pjpl.s7.command;

import Moka7.S7;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import pjpl.s7.common.BitsCode;
import pjpl.s7.common.CommandCode;
import pjpl.s7.process.Process;
import pjpl.s7.util.Bits;

/**
 *
 * @author pjanczura
 */
public class BitSwitch extends Command{

	public BitSwitch(byte processId, Socket socket) throws IOException {
		super(processId, socket);
	}

	@Override
	public short getCommandCode() {
		return CommandCode.BIT_SWITCH;
	}

	@Override
	public CommandResponse action(Process process) {

		try {
			switch(memType){
				case S7.D:
					varBuff = process.getMemClip().memD.varBuff(varCode);
					Bits.sw(varBuff, bitNr);
					process.getMemClip().memD.varBuff(bitDescription, varBuff);
					break;
				case S7.I:
					varBuff = process.getMemClip().memI.varBuff(varCode);
					Bits.sw(varBuff, bitNr);
					process.getMemClip().memI.varBuff(bitDescription, varBuff);
					break;
				case S7.Q:
					varBuff = process.getMemClip().memQ.varBuff(varCode);
					Bits.sw(varBuff, bitNr);
					process.getMemClip().memQ.varBuff(bitDescription, varBuff);
					break;
			}
			return new ResponseOk(getProcessId(), getCommandCode(), socket);
		} catch (IOException ex) {
			Logger.getLogger(BitOn.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	@Override
	protected void loadParameters() {
		try {
//			bitDescription = dataInputStream.readInt();
//			BitsCode.parse(bitDescription);
//			memType = BitsCode.memType;
//			varCode = BitsCode.varCode;
//			bitNr = BitsCode.bitNr;
			memType = dataInputStream.readByte();
			varCode = dataInputStream.readShort();
			bitNr = dataInputStream.readByte();
		} catch (IOException ex) {
			Logger.getLogger(D_SetByte.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	private int bitDescription;
	private byte memType;
	private short varCode;
	private byte bitNr;
	private byte[] varBuff;
}
