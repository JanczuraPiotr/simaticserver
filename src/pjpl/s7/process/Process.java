package pjpl.s7.process;

import Moka7.S7;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map.Entry;
import java.util.TreeMap;
import pjpl.s7.device.PLC;
import pjpl.s7.util.MemoryCell;
import pjpl.s7.util.MemoryClip;

/**
 */
 public abstract class Process implements Runnable{
	 public Process(){
		System.out.println("Process.constructor");
		plcs = new TreeMap<>();

		initPlcs();
		initMemory();
		memoryClip = new MemoryClip(memoryD, memoryI, memoryQ,0);
	}

	 /**
		* Uruchomienie operacji odczytu wszystkich bloków danych
		*/
	 protected void steepPrepareRead(){
		 PLC plc;
		 // @prace 00 Przerobić mem tak aby była jedną przestrzenią a nie mapą buforów
		 // W procesie jest więcej niż jeden PLC a każda grupa zmiennych wymaga osobnego odczytu.
		 // Odczytanie wszystkich bloków danych z jednego PLC w przechodząc z bloku do kolejnego bloku
		 // generuje zwłokę czasową związaną na oczekiwanie na wykonanie poprzedniego odczytu.
		 // Dla tego odczyt odbywa się z podziałem na bloki danych a nie na procesory dzięki temu w jednym czasie
		 // wszystkie procesory wykonują komunikację.
		 for( Entry<Integer, PLC> el : plcs.entrySet()){
			 plc = el.getValue();
			 plc.ReadArea(S7.D, 0, 0, plc.getSizeAreaD(), memoryD.getMemReference(el.getKey()));
		 }
		 for( Entry<Integer, PLC> el : plcs.entrySet()){
			 plc = el.getValue();
			 plc.ReadArea(S7.I, 0, 0, plc.getSizeAreaI(), memoryI.getMemReference(el.getKey()));
		 }
		 for( Entry<Integer, PLC> el : plcs.entrySet()){
			 plc = el.getValue();
			 plc.ReadArea(S7.Q, 0, 0, plc.getSizeAreaQ(), memoryQ.getMemReference(el.getKey()));
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
			memoryClip.timeStamp = System.currentTimeMillis();
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


	abstract protected void initMemory();
	abstract protected void initPlcs();
	protected void addPlc(PLC newPlc){
		plcs.put(Process1.id, newPlc);
	}


	//------------------------------------------------------------------------------
	protected MemoryD memoryD;
	protected MemoryI memoryI;
	protected MemoryQ memoryQ;
	protected MemoryClip memoryClip;
	// @todo opracować pozostałe bloki pamięci

	protected TreeMap<Integer, PLC> plcs;
	// Komórki pamięci zmienione podczas tego kroku. Będą wysłane do sterownika w metodzie steepWrite().
	protected TreeMap<Integer, MemoryCell> memDBChangedId;
	// Wyjścia zmienione podczas tego kroku. Będą wysłane do sterownika w metodzie steepWrite().
	protected TreeMap<Integer, MemoryCell> memOutChangedId;








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
