package pjpl.s7.process;

import Moka7.S7;
import Moka7.S7Client;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TreeMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import pjpl.s7.command.CommandResponse;
import pjpl.s7.common.ConstPLC;
import pjpl.s7.device.PLC;
import pjpl.s7.util.MemByteClip;
import pjpl.s7.util.MemCell;
import pjpl.s7.util.MemClip;

/**
 */
 public abstract class Process implements Runnable{
	 public static final int PLC_DB_BLOCK = 1;
	 public static final int PLC_INTPUT_BUFF_D = 0;
	 public static final int PLC_INTPUT_BUFF_I = 1;
	 public static final int PLC_INTPUT_BUFF_Q = 2;
	 public static final int PLC_INTPUT_BUFF_ = 3;
	 public static final int PLC_OPERATION_RESULT_READ_D = 0;
	 public static final int PLC_OPERATION_RESULT_READ_I = 1;
	 public static final int PLC_OPERATION_RESULT_READ_Q = 2;
	 public static final int PLC_OPERATION_RESULT_ = 3;

	 public Process(byte id){
		 this.id = id;
		commandQueue = new LinkedList<>();
		commandResponseQueue = new LinkedBlockingQueue<>();
		init();
		memClip = new MemClip(memD, memI, memQ);
	}

	 //------------------------------------------------------------------------------
	 // interfejs - początek

	 public byte id(){
		 return id;
	 }

	 public Queue<pjpl.s7.command.Command> getCommadQueue(){
		 return commandQueue;
	 }
	 public MemClip getMemClip(){
		 return memClip;
	 }
	 public MemByteClip getRaport(){
		 return memRaport;
	 }

	 // interfejs - koniec
	 //------------------------------------------------------------------------------


	 /**
		* Uruchomienie operacji odczytu wszystkich bloków danych
		*/
	 protected void steepPrepareRead(){
		 // W procesie jest więcej niż jeden PLC a każda grupa zmiennych wymaga osobnego odczytu.
		 // Odczytanie wszystkich bloków danych z jednego PLC w przechodząc z bloku do kolejnego bloku
		 // generuje zwłokę czasową związaną na oczekiwanie na wykonanie poprzedniego odczytu.
		 // Dla tego odczyt odbywa się z podziałem na bloki danych a nie na procesory dzięki temu w jednym czasie
		 // wszystkie procesory wykonują komunikację.

		 for( int plcIx = 0 ; plcIx < plcs.length ; plcIx++){
			 resultPlcsOperations[plcIx][PLC_OPERATION_RESULT_READ_D]
					 = plcs[plcIx].ReadArea(S7.D, 1, 0, memD.getSize(), inputPlcsBuffs[plcIx][PLC_INTPUT_BUFF_D]);
		 }
		 for( int i = 0 ; i < ConstPLC.COUNT ; i++){
			 resultPlcsOperations[i][PLC_OPERATION_RESULT_READ_I]
					 = plcs[i].ReadArea(S7.I, 0, 0, memI.getSize(), inputPlcsBuffs[i][PLC_INTPUT_BUFF_I]);
		 }
		 for( int i = 0 ; i < ConstPLC.COUNT ; i++){
			 resultPlcsOperations[i][PLC_OPERATION_RESULT_READ_Q]
					 = plcs[i].ReadArea(S7.Q, 0, 0, memQ.getSize(), inputPlcsBuffs[i][PLC_INTPUT_BUFF_Q]);
		 }
	 }
	 /**
		* Metoda musi zatrzymać pracę wątku na czas zakończenia wszystkich operacji uruchomionych w steepPrepareRead()
		*/
	 protected void steepWaitRead(){
		 // Obecny sposób wykonywania zapytań do sterownika kończy się w metodzie steepPrepareRead().
		 // Nie ma więc potrzeby zatrzymywania wątku na czas skompletowania odpowiedzi.
		 // Docelowo w metodzie steepPrepareRead() będą uruchamiane wątki komunikujące się ze sterownikami. Po wysłaniu
		 // wszystkich zapytań należy poczekać na aż zakończy się ostatnia komunikacja i dane prcosesowe zostaną
		 // skompletowane. Ta metoda powinna zawierać kod zatrzymujący prace wątku do tego czasu. Przed uruchomieniem
		 // testu na odbiór całości danych można tu wykonać dodatkowe zadania.
	 }
	 /**
		* Wykonane gdy zakończyły pracę wszystkie operacje odczytu danych.
		* Po tej metodzie wszystkie obiekty przechowujące stan pamięci i portów sterownika są aktualne.
		*/
	protected void steepRead(){
		for( int plcIx = 0 ; plcIx < inputPlcsBuffs.length ; plcIx++){
			memD.writeMem(inputPlcsBuffs[plcIx][PLC_INTPUT_BUFF_D], plcIx);
			memI.writeMem(inputPlcsBuffs[plcIx][PLC_INTPUT_BUFF_I], plcIx);
			memQ.writeMem(inputPlcsBuffs[plcIx][PLC_INTPUT_BUFF_Q], plcIx);
		}
		memRaport = new MemByteClip(memD.getMemCopy(), memI.getMemCopy(), memQ.getMemCopy(),msStartRun);
	};
	/**
	 * Główne operacje wątku. Tu można wykonanć obliczenia na zmniennych procesu.
	 */
	abstract protected void steep();
	/**
	 * Wykonywanie ewentualnych komend nadesłanych do procesu przez sieć.
	 */
	protected void steepCommands(){
		pjpl.s7.command.Command command;
		while( null != ( command = commandQueue.poll() )){
			commandResponseQueue.add(command.action(this));
		}
	}
	/**
	 * Zapisywanie zmodyfikowanych zmiennych do sterownika
	 */
	protected void steepWrite(){
		ArrayList<MemCell> modCells;

		if( ( modCells = memD.getModifiedCells() ) != null){
			byte[] mem = memD.getMem();
			modCells.forEach(
					(el)->{
						el.getPlc().WriteArea(S7.D, PLC_DB_BLOCK, el.getPos(), el.getOccupiedMemSpace(), mem);
					}
			);
			modCells.clear();
		}
		if( ( modCells = memI.getModifiedCells() ) != null){
			byte[] mem = memI.getMem();
			modCells.forEach(
					(el)->{
						el.getPlc().WriteArea(S7.I, 0, el.getPos(), el.getOccupiedMemSpace(), mem);
					}
			);
			modCells.clear();
		}
		if( ( modCells = memQ.getModifiedCells() ) != null){
			byte[] mem = memQ.getMem();
			modCells.forEach(
					(el)->{
						el.getPlc().WriteArea(S7.Q, 0, el.getPos(), el.getOccupiedMemSpace(), mem);
					}
			);
			modCells.clear();
		}
	};
	abstract protected void steepException(Exception e);
	abstract protected void steepExceptionBis(Exception eBis);
	abstract protected void steepExceptionFinally(Exception e);
	abstract protected void steepFinaly();
	/**
	 * Metoda wywoływana na zakończenie kroku w celu wykonania wstępnego przygotowania do następnego kroku, by właściwy
	 * kod następnego kroku wykonał się jak najszybciej po uruchomieniu wątku.
	 */
	protected void steepPrepareNextSteep(){};

	@Override
	public void run(){
		try{
			msStartRun =  System.currentTimeMillis();
			msSteepStart =  System.currentTimeMillis();
			steepPrepareRead();      msSteepPrepareRead = System.currentTimeMillis();
			steepWaitRead();         msSteepWaitRead = System.currentTimeMillis();
			memClip.timeStamp = System.currentTimeMillis();
			steepRead();             msSteepRead = System.currentTimeMillis();
			steep();                 msSteep = System.currentTimeMillis();
			steepCommands();         msSteepCommands = System.currentTimeMillis();
			steepWrite();            msSteepWrite = System.currentTimeMillis();
			steepPrepareNextSteep(); msSteepPrepareNextSteep = System.currentTimeMillis();
			msSteepEnd = System.currentTimeMillis();
		}catch(Exception e){
			try{
				steepException(e);
			}catch(Exception eBis){
				steepExceptionBis(eBis);
			}finally{
				steepExceptionFinally(e);
			}
		}finally{
			steepFinaly();

			for( int i = 0; i < resultPlcsOperations.length; i++){
				for( int j = 0 ; j < resultPlcsOperations[i].length ; j++ ){
					resultPlcsOperations[i][j] = S7Client.errIsOK;
				}
			}
			msSteepFinally = System.currentTimeMillis();
		}
//		podsumowanieSteep();
	}

	//------------------------------------------------------------------------------
	// metody chronione

	protected void addPlc(int codePLC, PLC newPlc){
		plcs[codePLC] = newPlc;
	}

	abstract protected void initMemory();
	abstract protected void initPlcs();
	protected void initInputBuff(){
		inputPlcsBuffs = new byte[plcs.length][PLC_INTPUT_BUFF_][];
		for( int i = 0; i < plcs.length; i++ ){
			inputPlcsBuffs[i][PLC_INTPUT_BUFF_D] = new byte[memD.getPlcMemSize(i)];
			inputPlcsBuffs[i][PLC_INTPUT_BUFF_I] = new byte[memI.getPlcMemSize(i)];
			inputPlcsBuffs[i][PLC_INTPUT_BUFF_Q] = new byte[memQ.getPlcMemSize(i)];
		}
		resultPlcsOperations = new int[plcs.length][PLC_OPERATION_RESULT_];
	}
	private void initCommandResposeThread(){
		commandResponseThread = new Thread(
				new Runnable() {
					@Override
					public void run() {
						CommandResponse commandResponse;
						while(true){
							try {
								commandResponse = commandResponseQueue.take();
								commandResponse.sendToStream();
							} catch (InterruptedException ex) {
								Logger.getLogger(Process.class.getName()).log(Level.SEVERE, null, ex);
							} catch (IOException ex) {
								Logger.getLogger(Process.class.getName()).log(Level.SEVERE, null, ex);
							}
						}
					}
			}
		);
		commandResponseThread.start();
	}

	//------------------------------------------------------------------------------
	// metody prywatne

	private void init(){
		initPlcs();
		initMemory();
		initInputBuff();
		initCommandResposeThread();
	}

	//------------------------------------------------------------------------------
	// atrybuty chronione
	protected long msStartRun;
	protected byte id;
	protected D_Mem memD;
	protected I_Mem memI;
	protected Q_Mem memQ;
	protected MemClip memClip;
	protected MemByteClip memRaport;
	// @todo opracować pozostałe bloki pamięci

	// Sterowniki obsługujące process.
	protected PLC[] plcs = new PLC[ConstPLC.COUNT];

	// Komórki pamięci zmienione podczas tego kroku. Będą wysłane do sterownika w metodzie steepWrite().
	protected TreeMap<Integer, MemCell> memDBChangedId;
	// Wyjścia zmienione podczas tego kroku. Będą wysłane do sterownika w metodzie steepWrite().
	protected TreeMap<Byte, MemCell> memOutChangedId;

	// Kolejka na odebrane komendy.
	// Komendy odbierane są w bocznym wątku i wstawiane do tej kolejki. Przetwarzanie odbywa się w głównym wątku procesu.
	protected Queue<pjpl.s7.command.Command> commandQueue;
	// Kolejka na odpowiedzi wygenerowane przez komendy.
	// Odpowiedzi od komend wysyłane są w wątku bocznym uruchamianym gdy w kolejce znajduje się jakiś obiekt.
	protected LinkedBlockingQueue<pjpl.s7.command.CommandResponse> commandResponseQueue;
	// Wątek odsyłający odpowiedzi na wykonane komendy
	protected Thread commandResponseThread;

	//------------------------------------------------------------------------------
	// atrybuty prywatne

	// Tablica buforów wejściowych dla wszystkich PLC
	private byte[][][] inputPlcsBuffs;
	private int[][] resultPlcsOperations;




	//------------------------------------------------------------------------------
	// na czas projektowania
	protected final DateFormat datePCFormat = new SimpleDateFormat(pjpl.s7.run.SimaticServer.config.getProperty("format_dateMS"));
	protected String summaryRun;
	protected long msSteepStart = 0;
	protected long msSteepPrepareRead = 0;
	protected long msSteepWaitRead = 0;
	protected long msSteepRead = 0;
	protected long msSteep = 0;
	protected long msSteepCommands = 0;
	protected long msSteepWrite = 0;
	protected long msSteepPrepareNextSteep = 0;
	protected long msSteepEnd = 0;
	protected long msSteepFinally = 0;
	protected void podsumowanieSteep(){
		System.out.println("------------------------------------------------------------------------------");
		System.out.println("Podsumowanie zadania : " + getClass().getName());
		System.out.println( datePCFormat.format(msSteepStart) + " Process.msSteepStart");
		System.out.println( datePCFormat.format(msSteepPrepareRead)
				+ " Process.msSteepPrepareRead       "
				+ String.format("%4d", msSteepPrepareRead - msSteepStart) + " [ms]");
		System.out.println( datePCFormat.format(msSteepWaitRead)
				+ " Process.msSteepWaitRead          "
				+ String.format("%4d", msSteepWaitRead - msSteepPrepareRead) + " [ms]");
		System.out.println( datePCFormat.format(msSteepRead)
				+ " Process.msSteepRead              "
				+ String.format("%4d",  msSteepRead - msSteepWaitRead) + " [ms]");
		System.out.println( datePCFormat.format(msSteep)
				+ " Process.msSteep                  "
				+ String.format("%4d", msSteep - msSteepRead) + " [ms]");
		System.out.println(datePCFormat.format(msSteepCommands)
				+ " Process.msSteepCommands          "
				+ String.format("%4d", msSteepCommands - msSteep ) + " [ms]" );
		System.out.println( datePCFormat.format(msSteepWrite)
				+ " Process.msSteepWrite             "
				+ String.format("%4d", msSteepWrite - msSteepCommands) + " [ms]");
		System.out.println( datePCFormat.format(msSteepPrepareNextSteep)
				+ " Process.msSteepPrepareNextSteep  "
				+ String.format("%4d", msSteepPrepareNextSteep - msSteepWrite) + " [ms]" );
		System.out.println( datePCFormat.format(msSteepEnd)
				+ " Process.msSteepEnd               "
				+ String.format("%4d", msSteepEnd - msSteepPrepareNextSteep) + " [ms]") ;
		System.out.println(datePCFormat.format(msSteepFinally)
				+ " Process.msSteepFinally           "
				+ String.format("%4d", msSteepFinally - msSteepEnd) + " [ms]");
		System.out.println(datePCFormat.format(msSteepFinally)
				+ " Process.run                      "
				+ String.format("%4d", msSteepFinally - msSteepStart) + " [ms]");

	}

}
