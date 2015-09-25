package pjpl.s7.command;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import pjpl.s7.common.CommandCode;

/**
 *
 * @author pjanczura
 */
public class CommandBuilder {
	public CommandBuilder(){
	}
	public Command build(short code, byte processId, DataInputStream inputStream,OutputStream outputStream) throws IOException{
		Command command = null;
		System.out.printf("CommandBuilder.build() code = %02X addr = %02X\n", code, processId);

		switch(code){
			// if(  0000  0x0000 < code && code < 0x1000   4096 )
			// if(  4095  0x0FFF < code && code < 0x2000   8192 )
			// if(  8191  0x1FFF < code && code < 0x4000  16384 )
			// if( 16383  0x3FFF < code && code < 0x8000  32768 )

			// if( 32767  0x7FFF < code && code < 0xC000  49152 )

			case (short)CommandCode.GET_I_BYTE: command = new Get_I_Byte(processId, inputStream, outputStream); break;
			case (short)CommandCode.GET_Q_BYTE: command = new Get_Q_Byte(processId, inputStream, outputStream); break;
			case (short)CommandCode.SET_Q_BYTE: command = new Set_Q_Byte(processId, inputStream, outputStream); break;
			case (short)CommandCode.GET_D_BYTE: command = new Get_D_Byte(processId, inputStream, outputStream); break;
			case (short)CommandCode.SET_D_BYTE: command = new Set_D_Byte(processId, inputStream, outputStream); break;
			case (short)CommandCode.GET_D_INT : command = new Get_D_Int( processId, inputStream, outputStream); break;
			case (short)CommandCode.SET_D_INT : command = new Set_D_Int( processId, inputStream, outputStream); break;
			case (short)CommandCode.GET_D_DINT: command = new Get_D_DInt(processId, inputStream, outputStream); break;
			case (short)CommandCode.SET_D_DINT: command = new Set_D_DInt(processId, inputStream, outputStream); break;
			case (short)CommandCode.GET_D_REAL: command = new Get_D_Real(processId, inputStream, outputStream); break;
			case (short)CommandCode.SET_D_REAL: command = new Set_D_Real(processId, inputStream, outputStream); break;

			// if( 49151  0xBFFF < code && code < 0xFFFF  65535 )

			default:
				command = new CommandNull(processId, inputStream,outputStream);
		}
		return command;
	}


}
