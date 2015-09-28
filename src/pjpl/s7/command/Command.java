package pjpl.s7.command;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author Piotr Janczura <piotr@janczura.pl>
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
	public byte getProcesId(){
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
	protected byte processId;

}
