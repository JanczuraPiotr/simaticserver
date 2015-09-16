package pjpl.s7.process;

import Moka7.S7;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TreeMap;
import pjpl.s7.common.ConstPLC;
import pjpl.s7.device.PLC;
import pjpl.s7.util.MemCell;
import pjpl.s7.util.MemClip;

/**
 */
 public abstract class Process implements Runnable{
	 public Process(){
		System.out.println("Process.constructor");

		init();
		memClip = new MemClip(memD, memI, memQ);
	}

	 /**
		* Uruchomienie operacji odczytu wszystkich bloków danych
		*/
	 protected void steepPrepareRead(){
		 // W procesie jest więcej niż jeden PLC a każda grupa zmiennych wymaga osobnego odczytu.
		 // Odczytanie wszystkich bloków danych z jednego PLC w przechodząc z bloku do kolejnego bloku
		 // generuje zwłokę czasową związaną na oczekiwanie na wykonanie poprzedniego odczytu.
		 // Dla tego odczyt odbywa się z podziałem na bloki danych a nie na procesory dzięki temu w jednym czasie
		 // wszystkie procesory wykonują komunikację.

		 for( int i = 0 ; i < ConstPLC.COUNT ; i++){
			 resultInputD = plcs[i].ReadArea(S7.D, 0, 0, memD.getSize(), inputBuffD);
		 }
		 for( int i = 0 ; i < ConstPLC.COUNT ; i++){
			 resultInputI = plcs[i].ReadArea(S7.I, 0, 0, memI.getSize(), inputBuffI);
		 }
		 for( int i = 0 ; i < ConstPLC.COUNT ; i++){
			 resultInputQ = plcs[i].ReadArea(S7.Q, 0, 0, memQ.getSize(), inputBuffQ);
		 }
	 }
	 /**
		* Metoda musi zatrzymać pracę wątku na czas zakończenia wszystkich operacji uruchomionych w steepPrepareRead()
		*/
	 protected void steepWaitRead(){
		 // Obecny sposób wykonywania zapytań do sterownika kończy się w metodzie steepPrepareRead().
		 // Nie ma więc potrzeby zatrzymywania wątku na czas skompletowania odpowiedzi
	 }
	 /**
		* Wykonane gdy zakończyły pracę wszystkie operacje odczytu danych.
		* Po tej metodzie wszystkie obiekty przechowujące stan pamięci i portów sterownika są aktualne.
		*/
	 protected void steepRead(){
		 // Do czasu zmodyfikowania obiektu S7Client całą operację pobrania danych ze sterownika wykonywana jest w metodzie
		 // steepPrepareRead() w sposób blokujący.
		 // @prace 00 odebrać wczytane dane;
	 };
	/**
	 * Główne operacje wątku
	 */
	abstract protected void steep();
	/**
	 * Zapisywanie zmodyfikowanych zmiennych do sterownika
	 */
	protected void steepWrite(){

	};
	abstract protected void steepException(Exception e);
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
			msSteepStart =  System.currentTimeMillis();
			steepPrepareRead();      msSteepPrepareRead = System.currentTimeMillis();
			steepWaitRead();         msSteepWaitRead = System.currentTimeMillis();
			memClip.timeStamp = System.currentTimeMillis();
			steepRead();             msSteepRead = System.currentTimeMillis();
			steep();                 msSteep = System.currentTimeMillis();
			steepWrite();            msSteepWrite = System.currentTimeMillis();
			steepPrepareNextSteep(); msSteepPrepareNextSteep = System.currentTimeMillis();
			msSteepEnd = System.currentTimeMillis();
		}catch(Exception e){
			try{
				steepException(e);
			}catch(Exception eBis){
				steepExceptionFinally(e);
			}
		}finally{
			steepFinaly();
			msSteepFinally = System.currentTimeMillis();
		}
		podsumowanieSteep();
	}

	//------------------------------------------------------------------------------
	// metody chronione

	protected void addPlc(int codePLC, PLC newPlc){
		plcs[codePLC] = newPlc;
	}

	abstract protected void initMemory();
	abstract protected void initPlcs();
	protected void initTempBuff(){
		inputBuffD = new byte[memD.getSize()];
		inputBuffI = new byte[memI.getSize()];
		inputBuffQ = new byte[memQ.getSize()];
	}

	//------------------------------------------------------------------------------
	// metody prywatne

	private void init(){
		initPlcs();
		initMemory();
		initTempBuff();
	}

	//------------------------------------------------------------------------------
	// atrybuty chronione

	protected MemD memD;
	protected MemI memI;
	protected MemQ memQ;
	protected MemClip memClip;
	// @todo opracować pozostałe bloki pamięci

	// Sterowniki obsługujące process.
	protected PLC[] plcs = new PLC[ConstPLC.COUNT];

	// Komórki pamięci zmienione podczas tego kroku. Będą wysłane do sterownika w metodzie steepWrite().
	protected TreeMap<Integer, MemCell> memDBChangedId;
	// Wyjścia zmienione podczas tego kroku. Będą wysłane do sterownika w metodzie steepWrite().
	protected TreeMap<Integer, MemCell> memOutChangedId;


	//------------------------------------------------------------------------------
	// atrybuty prywatne

	private byte[] inputBuffD;
	private byte[] inputBuffI;
	private byte[] inputBuffQ;
	private int resultInputD;
	private int resultInputI;
	private int resultInputQ;







	//------------------------------------------------------------------------------
	// na czas projektowania
	protected final DateFormat datePCFormat = new SimpleDateFormat(pjpl.s7.run.SimaticServer.config.getProperty("format_dateMS"));
	protected String summaryRun;
	protected long msSteepStart = 0;
	protected long msSteepPrepareRead = 0;
	protected long msSteepWaitRead = 0;
	protected long msSteepRead = 0;
	protected long msSteep = 0;
	protected long msSteepWrite = 0;
	protected long msSteepPrepareNextSteep = 0;
	protected long msSteepEnd = 0;
	protected long msSteepFinally = 0;
	protected void podsumowanieSteep(){
		System.out.println("------------------------------------------------------------------------------");
		System.out.println("Podsumowanie zadania process");
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
		System.out.println( datePCFormat.format(msSteepWrite)
				+ " Process.msSteepWrite             "
				+ String.format("%4d", msSteepWrite - msSteep) + " [ms]");
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
