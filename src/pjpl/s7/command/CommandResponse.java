package pjpl.s7.command;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Klasa bazowa dla klas opakowujących dane wygenerowane jako odpowiedź na komendę obsługiwaną w obiekcie klasy Command.
 * Interfejs tej klasy jest wystarczający do generowania odpowiedzi wysyłających kod liczbowy.
 * Dla bardziej skomplikowanych odpowiedzi należy rozbudować klasę o te dodatkowe zmienne i metody dostępu.
 *
 * Nagłówek bufora - stała zawartość dla każdego pochodnego CommandResponse
 * buff[0] identyfikator procesu który wygenerował odpowiedź
 * buff[1..2] kod komendy która była obsługiwana przez process
 * buff[3..5] kod odpowiedzi z jaką zakończyła się praca komendy na którą wygenerowano tą opowiedź
 * jeżeli buff[3..5] == 0x0000 odpowiedź negatywna na komendę wymagającą tylko potwierdzenia lub niepowodzenie komendy
 * jeżeli buff[3..5] == 0xFFFF odpowiedź tak na komendę wymagającą tylko potwierdzenia
 * jeżeli 0x0000 < buff[3..5] < 0xFFFF specyficzna odpowiedź od której mogą zależeć pozostałe parametry bufora.
 * W zależności od klasy pochodnej mogą być dołączane kolejne parametry definiowane w klasach pochodnych
 */
public abstract class CommandResponse {
	/**
	 * @param processId identyfikator procesu do którego przesłana była komenda która wygenerowała odpowiedź.
	 * @param commandCode kod komendy która wygenerowała odpowiedź.
	 * @param responseCode kod zwracanej odpowiedzi.
	 * @param socket gniazdko do którego CommandResponse ma przesłać treść odpowiedzi.
	 */
	public CommandResponse(byte processId, short commandCode, short responseCode, Socket socket) throws IOException{
		this.processId = processId;
		this.commandCode = commandCode;
		this.responseCode = responseCode;
		this.socket = socket;
	}

	//------------------------------------------------------------------------------
	// interfejs

	public final short getCommandCode(){
		return commandCode;
	};
	public final byte getProcessId(){
		return processId;
	}
	/**
	 * Kod odpowiedzi.
	 * Pokrywana w klasie pochodnej i zwraca odpowiednią wartość z zakresu CommandCode.XXX
	 */
	public short getResponseCode(){
		return responseCode;
	}

	/**
	 * Wysyła do gniazdka bufor z danymi.
	 * Zamyka strumień.
	 */
	public final void sendToStream(){
		calculateBuffSize();
		buff = new byte[getBuffSize()];
		prepareBuff();
		try {
			outputStream.write(buff);
			outputStream.close();
		} catch (IOException ex) {
			Logger.getLogger(ResponseOk.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	// interfejs
	//------------------------------------------------------------------------------


	//------------------------------------------------------------------------------
	// metody chronione


	/**
	 * Nadaje wartość zmiennej buffSize.
	 */
	protected abstract void calculateBuffSize();
	/**
	 * Rozkład na bajty wszystkich atrybutów obiektu i umieszczenie ich w buforze.
	 */
	protected abstract void prepareBuff();
	protected byte[] getBuff(){
		return buff;
	}
	protected int getBuffSize(){
		return buffSize;
	};

	// metody chronione
	//------------------------------------------------------------------------------
	// atrybuty chronione

	// Kod komendy która wygenerowała odpowiedź
	protected byte[] buff = null;
	protected int buffSize = 0;
	protected Socket socket;
	protected OutputStream outputStream;

	// atrybuty chronione
	//------------------------------------------------------------------------------
	// metody prywatne

	// metody prywatne
	//------------------------------------------------------------------------------
	// atrybuty prywatne

	private final byte processId;
	private final short commandCode;
	private final short responseCode;

	// atrybuty prywatne
	//------------------------------------------------------------------------------

}
