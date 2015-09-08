package pjpl.s7.command;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * @author Piotr Janczura <piotr@janczura.pl>
 */
public abstract class Command { // _cmd
	private static int codeInc = 0;

	// Stałe ogólne
	// 000000000000000B -> 0000111111111111B => 0x0000 -> 0x0FFF => 0 ->  4095
	public static final int OK = 0x0000;
	public static final int NO = 0x0001;

	// Stałe wewnętrzne biblioteki pjpl.s7
	// 000100000000000B -> 0001111111111111B => 0x1000 -> 0x1FFF => 4096 ->  8191

	// nieprzydzielone:
	// 001000000000000B -> 0011111111111111B => 0x2000 -> 0x3FFF =>   8192 -> 16383


	// Kody od 0x8000 wydzielone są dla klas definiujących algorytmy do wykonania w ramach procesu do którego są skierowane.
	// Kod może wymagać parametrów do pracy i te umieszczone są w tym samym buforze zaraz za nim. Obsługą kody wraz z
	// odczytaniem parametrów i wykonaniem algorytmu w ramach procesu zajmują się obiekty klas pochodnych po Command.

	// Kody komend nie zależnych od procesu.
	// 100000000000000B -> 1011111111111111B => 0x8000 -> 0xBFFF =>  32768 -> 49151
	static {codeInc = 0x8000;}

	public static final int GET_BIT_IN  = codeInc++;
	public static final int GET_BIT_OUT = codeInc++;
	public static final int GET_BIT     = codeInc++;
	public static final int GET_BYTE    = codeInc++;
	public static final int GET_WORD    = codeInc++;

	public static final int SET_BIT_OUT = codeInc++;
	public static final int SET_BIT     = codeInc++;
	public static final int SET_BYTE    = codeInc++;
	public static final int SET_WORD    = codeInc++;


	// Kody komend indywidualne dla tej aplikacji
	// 110000000000000B -> 1111111111111111B => 0xC000 -> 0xCFFF =>  49152 -> 65535
	static {codeInc = 0xC000;}



	public Command(DataInputStream commandInputStream) throws IOException{
		this.commandInputStream = commandInputStream;
		this.processId = this.commandInputStream.readByte();
		this.deviceId = this.commandInputStream.readByte();
	}
	public byte getProcesId(){
		return (byte) processId;
	}
	public byte getDeviceId(){
		return (byte) deviceId;
	}
	protected abstract void prepareContent();

	protected final DataInputStream commandInputStream;
	/**
	 * Identyfikator procesu, który powinien wykonać komendę
	 * Wartość zmiennej przekazywana jest w parametrach komendy w buforze za kodem Komendy.
	 */
	protected int processId;
	/**
	 * Identyfikator sterownika do którego może odnosić się komenda.
	 * Wartość zmiennej przekazywana jest w parametrach komendy w buforze zaraz za identyfikatorem procesu.
	 * Wartość -1 oznacza że algorytm nie jest kierowany do konkretnego sterownika
	 */
	protected int deviceId = -1 ;
}
