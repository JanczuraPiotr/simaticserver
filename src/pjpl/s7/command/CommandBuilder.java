package pjpl.s7.command;

import java.io.DataInputStream;
import java.io.IOException;
import pjpl.s7.common.CommandCode;

/**
 *
 * @author pjanczura
 */
public class CommandBuilder {
	public CommandBuilder(){}
	public Command build(int code, DataInputStream input) throws IOException{
		Command command = null;

		switch(code){
			// if(  0000  0x0000 < code && code < 0x1000   4096 )
			// if(  4095  0x0FFF < code && code < 0x2000   8192 )
			// if(  8191  0x1FFF < code && code < 0x4000  16384 )
			// if( 16383  0x3FFF < code && code < 0x8000  32768 )
			// if( 32767  0x7FFF < code && code < 0xC000  49152 )
			case CommandCode.SET_BYTE:
				break;
			// if( 49151  0xBFFF < code && code < 0xFFFF  65535 )

			default:
				command = new CommandNull(input);
		}
		return command;
	}


}
