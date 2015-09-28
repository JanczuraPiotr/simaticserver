package pjpl.s7.command;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Klasa bazowa dla klas opakowujących dane wygenerowane jako odpowiedź na komendę obsługiwaną w obiekcie klasy Command.
 * Interfejs tej klasy jest wystarczający do generowania odpowiedzi wysyłających kod liczbowy.
 * Dla bardziej skomplikowanych odpowiedzi należy rozbudować klasę o te dodatkowe zmienne i metody dostępu.
 */
public abstract class CommandResponse {
	/**
	 * @param processId identyfikator procesu do którego przesłana była komenda która wygenerowała odpowiedź.
	 * @param commandCode kod komendy która wygenerowała odpowiedz
	 * @param outputStream strumień do którego CommandResponse ma przesłać treść odpowiedzi.
	 */
	public CommandResponse(byte processId, short commandCode, Socket socket) throws IOException{
		this.commandCode = commandCode;
		this.processId = processId;
		this.socket = socket;
		this.outputStream = socket.getOutputStream();
		init();
	}

	//------------------------------------------------------------------------------
	// interfejs

	public short getCommandCode(){
		return commandCode;
	};
	public byte getProcessId(){
		return processId;
	}
	/**
	 * Kod odpowiedzi.
	 * Pokrywana w klasie pochodnej i zwraca odpowiednią wartość z  CommandCode.XXX
	 */
	public abstract short getResponseCode();

	/**
	 * Rozkałada atrybuty odpowiedzi na bajty i wysyła je strumieniem.
	 * Zamyka strumień.
	 */
	public abstract void sendToStream();
	public byte[] getBuff(){
		return buff;
	}
	/**
	 * Zwraca rozmiar bufora koniecznego do utworzenia zrzutu zmiennych opisujących odpowiedź.
	 * @return
	 */
	public int getBuffSize(){
		return buffSize;
	}

	// interfejs
	//------------------------------------------------------------------------------


	//------------------------------------------------------------------------------
	// metody chronione

	// metody chronione
	//------------------------------------------------------------------------------
	// atrybuty chronione

	// Kod komendy która wygenerowała odpowiedź
	protected byte[] buff;
	protected int buffSize = 5;
	protected Socket socket;
	protected OutputStream outputStream;

	// atrybuty chronione
	//------------------------------------------------------------------------------
	// metody prywatne

	private void init(){
		buff = new byte[getBuffSize()];
	}

	// metody prywatne
	//------------------------------------------------------------------------------
	// atrybuty prywatne

	private final short commandCode;
	private final byte processId;

	// atrybuty prywatne
	//------------------------------------------------------------------------------

}
