package pjpl.s7.command;

import java.io.IOException;
import java.net.Socket;
import pjpl.s7.common.CommandCode;

/**
 * Klasa budująca komendę do wykonania na procesie na podstawie strumienia nadesłanego gniazdem z SimaticWeb lub
 * SimaticDesctop.
 *
 *
 * @author Piotr Janczura <piotr@janczura.pl>
 */
public class CommandBuilder {
	public CommandBuilder(){
	}
	public Command build(short code, byte processId, Socket socket) throws IOException{
		Command command = null;
		System.out.printf("CommandBuilder.build() code = %02X addr = %02X\n", code, processId);

		switch(code){
			// if(  0000  0x0000 < code && code < 0x1000   4096 )
			// if(  4095  0x0FFF < code && code < 0x2000   8192 )
			// if(  8191  0x1FFF < code && code < 0x4000  16384 )
			// if( 16383  0x3FFF < code && code < 0x8000  32768 )

			// if( 32767  0x7FFF < code && code < 0xC000  49152 )

			case (short)CommandCode.I_GET_BYTE: command = new I_GetByte(processId, socket); break;
			case (short)CommandCode.Q_GET_BYTE: command = new Q_GetByte(processId, socket); break;
			case (short)CommandCode.Q_SET_BYTE: command = new Q_SetByte(processId, socket); break;
			case (short)CommandCode.D_GET_BYTE: command = new D_GetByte(processId, socket); break;
			case (short)CommandCode.D_SET_BYTE: command = new D_SetByte(processId, socket); break;
			case (short)CommandCode.D_GET_INT : command = new D_GetInt( processId, socket); break;
			case (short)CommandCode.D_SET_INT : command = new D_SetInt( processId, socket); break;
			case (short)CommandCode.D_GET_DINT: command = new D_GetDInt(processId, socket); break;
			case (short)CommandCode.D_SET_DINT: command = new D_SetDInt(processId, socket); break;
			case (short)CommandCode.D_GET_REAL: command = new D_GetReal(processId, socket); break;
			case (short)CommandCode.D_SET_REAL: command = new D_SetReal(processId, socket); break;

			// if( 49151  0xBFFF < code && code < 0xFFFF  65535 )

			default:
				command = new CommandNull(processId, socket);
		}
		return command;
	}


}
