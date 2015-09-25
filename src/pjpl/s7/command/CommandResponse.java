package pjpl.s7.command;

import java.io.OutputStream;

/**
 */
public abstract class CommandResponse {
	/**
	 * @param commandCode kod komendy która wygenerowała odpowiedz
	 * @param outputStream strumień do którego CommandResponse ma przesłać treść odpowiedzi.
	 */
	public CommandResponse(short commandCode, OutputStream outputStream){
		this.commandCode = commandCode;
	}
	public short getCommandCode(){
		return commandCode;
	};
	public abstract short getResponseCode();

	private short commandCode;
}
