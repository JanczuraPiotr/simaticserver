package pjpl.s7.command;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Piotr Janczura <piotr@janczura.pl>
 */
public class BuilderCommand {
	public static final int READ_DB_BLOCK   = 1;
	public static final int READ_DB_BYTE    = 2;
	public static final int READ_DB_WORD    = 3;
	public static final int WRITE_DB_BLOCK  = 4;
	public static final int WRITE_DB_BYTE   = 5;
	public static final int WRITE_DB_WORD   = 6;

	public static BuilderCommand getInstance(InputStream commandStream){
		if( instance == null){
			instance = new BuilderCommand(commandStream);
		}
		return instance;
	}
	public Command getCpmmand() throws IOException{
		int commandCode;
		Command command = new CommandNull(commandInputStream);

		try {
			commandCode = commandInputStream.readInt();
			switch(commandCode){
//				case READ_DB_BLOCK:
//					command = new Command(commandInputStream);
//					break;
//				case READ_DB_BYTE:
//					command = new Command(commandInputStream);
//					break;
//				case READ_DB_WORD:
//					command = new Command(commandInputStream);
//					break;
//				case WRITE_DB_BLOCK:
//					command = new Command(commandInputStream);
//					break;
				case WRITE_DB_BYTE:
					command = new BramaWriteDBByte(commandInputStream);
					break;
				case WRITE_DB_WORD:
					command = new BramaWriteDBWord(commandInputStream);
					break;
			}
		} catch (IOException ex) {
			Logger.getLogger(BuilderCommand.class.getName()).log(Level.SEVERE, null, ex);
		}
		return command;
	}
	private BuilderCommand(InputStream commandStream){
		commandInputStream = new DataInputStream(commandStream);
	}

	private final DataInputStream commandInputStream;
	private static BuilderCommand instance = null;
}
/**
 * @prace prawdopodobnie singleton dla tej klasy nie jest wskazany ze względu na urzycie jej wewnątrz wątków.
 */