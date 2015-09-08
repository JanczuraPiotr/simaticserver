package pjpl.s7.command;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Buduje Komendę do wykonania przez process.
 *
 * Proces musi być przygotowany na jej wykonanie, w innym przypadku zgłasza wyjątek.
 * Zródłem komendy jest strumień binarny nadesłany na wejście servera z dowlnego źródła, np. przez połączenie sieciowe.
 * Pierwszym elementem strumienia jest kod komendy zapisany na dwóch bajtach. Jeżeli kod nie jest znany zgłaszany jest
 * wyjątek.
 *
 * @author Piotr Janczura <piotr@janczura.pl>
 * @todo Usupełnić komentarz o nazwy wyjątków po ich zaprojektowaniu.
 */
public class BuilderProcessCommand {
	public static final int READ_DB_BLOCK   = 1;
	public static final int READ_DB_BYTE    = 2;
	public static final int READ_DB_WORD    = 3;
	public static final int WRITE_DB_BLOCK  = 4;
	public static final int WRITE_DB_BYTE   = 5;
	public static final int WRITE_DB_WORD   = 6;


	/**
	 * Na podstawie strumienia wejściowego tworzy obiekt komendy do wykonania przez proces.
	 *
	 * @param commandStream Strumien binarny z definicją komendy
	 * @return
	 * @throws IOException
	 * @todo Zaprojektować wyjątki na wypadeg złego kodu komendy
	 */
	public Command getCommand(InputStream commandStream) throws IOException{
		int commandCode;
		commandInputStream = new DataInputStream(commandStream);
		//@todo Zastąpić zwracanie pustego komunikatu rzucaniem wyjątku
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
//				default:
					//@todo wyrzucić wyjątek o nie znanym kodzie komendy
			}
		} catch (IOException ex) {
			Logger.getLogger(BuilderProcessCommand.class.getName()).log(Level.SEVERE, null, ex);
		}
		return command;
	}

	private DataInputStream commandInputStream;
}
/**
 *
 */