package pjpl.s7.command;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Piotr Janczura <piotr@janczura.pl>
 */
abstract public class Command { // _cmd

	/**
	 * @param processId identyfikator procesu dla którym ma być wykonana komenda
	 * @param commandInputStream strumień z danymi dla komendy
	 * @param outputStream strumień do którego komenda ma przesłać odpowiedź. Minimalną odwpowiedzią może być OK lub NO
	 * @throws IOException
	 */
	public Command(byte processId, DataInputStream commandInputStream, OutputStream outputStream) throws IOException{
		this.processId = processId;
		this.commandInputStream = commandInputStream;
		this.outputStream = outputStream;
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
	 * Na pamięci przekazanej za pomocą memClip wykonyje zadania opisane w swojej definicji.
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

	/**
	 * Strumień w którym znajdują się elementy komendy
	 */
	protected final DataInputStream commandInputStream;
	protected final OutputStream outputStream;
	/**
	 * Identyfikator procesu, który powinien wykonać komendę
	 * Wartość zmiennej przekazywana jest w parametrach komendy w buforze za kodem Komendy.
	 */
	protected byte processId;

}
