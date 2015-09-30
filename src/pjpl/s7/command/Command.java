package pjpl.s7.command;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Komenda nadesłana do procesu.
 * Komenda przychodzi w postaci bufora przez socket.
 *
 * Domyślny format bufora - stała zawartość dla każdego pochodnego Command
 * buff[0]
 * jeżeli 0x00 < buff[0] < 0xFF identyfikator procesu do którego skierowana jest konenda
 *		buff[1..2] kod komendy
 * jeżeli 0x00 = buff[0] - komenda dotyczy SimaticServer
 * jeżeli 0xff = buff[0] - komenda dotyczy Klienta. Nie jest tym samym co odpowiedź na komendę.
 *
 * W zależności od klasy pochodnej mogą być dołączane kolejne parametry definiowane w klasach pochgodnych
 *
 */
abstract public class Command { // _cmd

	/**
	 * @param processId identyfikator procesu dla którym ma być wykonana komenda
	 * @param socket do którego komenda ma przesłać odpowiedź. Minimalną odpowiedzią może być OK lub NO
	 * @throws IOException
	 */
	public Command(byte processId, Socket socket) throws IOException{
		this.processId = processId;
		this.socket = socket;
		this.inputStream = socket.getInputStream();
		this.outputStream = socket.getOutputStream();
		this.dataInputStream = new DataInputStream(inputStream);
		this.init();
	}
	/**
	 * Zwraca kod komendy obsługiwanej przez obiekt;
	 * @return
	 */
	public abstract short getCommandCode();
	public byte getProcessId(){
		return  processId;
	}
	/*
	 * Na pamięci przekazanej za pomocą memClip wykonuje zadania opisane w swojej definicji.
	 */
	public abstract CommandResponse action(pjpl.s7.process.Process process);
	/**
	 * Na podstawie commandInputStream pobiera wartości parametrów dla komendy.
	 * Typy i ilość parametrów są indywidualną cechą każdej komendy.
	 */
	protected abstract void loadParameters();

	private void init(){
		loadParameters();
	}

	//------------------------------------------------------------------------------

	protected final Socket socket;
	protected final OutputStream outputStream;
	protected final InputStream inputStream;
	protected final DataInputStream dataInputStream;
	/**
	 * Identyfikator procesu, który powinien wykonać komendę
	 * Wartość zmiennej przekazywana jest w parametrach komendy w buforze za kodem Komendy.
	 */
	private byte processId;

}
