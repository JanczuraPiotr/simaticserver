package pjpl.s7.process;

import Moka7.S7;
import java.util.Map.Entry;
import java.util.TreeMap;
import pjpl.s7.device.PLC;
import pjpl.s7.util.MemoryCell;

/**
 */
 public abstract class Process implements Runnable{
	 public Process(){
		System.out.println("Process.constructor");
		plcs = new TreeMap<>();

		initPlcs();
		initMemory();

		int sizeI = 0;
		int sizeQ = 0;
		int sizeD = 0;
		for( Entry<Integer, PLC> el : plcs.entrySet()){
			sizeI += el.getValue().getSizeAreaI();
			sizeQ += el.getValue().getSizeAreaQ();
			sizeD += el.getValue().getSizeAreaD();
		}
		d = new byte[sizeD];
		i = new byte[sizeI];
		q = new byte[sizeQ];

	 }

	 /**
		* Uruchomienie operacji odczytu wsztstkich bloków danych
		*/
	 public void steepPrepareRead(){
		 PLC plc;
		 for( Entry<Integer, PLC> el : plcs.entrySet()){
			 plc = el.getValue();
			 plc.ReadArea(S7.D, 0, 0, plc.getSizeAreaD(), memoryD.getMem(el.getKey()));
			 plc.ReadArea(S7.I, 0, 0, plc.getSizeAreaI(), memoryI.getMem(el.getKey()));
			 plc.ReadArea(S7.Q, 0, 0, plc.getSizeAreaQ(), memoryQ.getMem(el.getKey()));
		 }
	 }
	 /**
		* Metoda musi zatrzymać pracę wątku na czas zakończenia wszystkich operacji uruchomionych w steepPrepareRead()
		*/
	 public void steepWaitRead(){
		 // Obecny sposób wykonywania zapytań do sterownika kończy się w metodzie steepPrepareRead().
		 // Nie ma więc potrzeby zatrzymywania wątku na czas skompletowania odpowiedzi
	 }
	 /**
		* Wykonane gdy zakończyły pracę wszystkie operacje odczytu danych.
		* Po tej metodzie wszystkie obiekty przchowujące stan pamięci i portów sterownika są aktualne.
		*/
	 public void steepRead(){
		 // Do czasu smodyfikowania obiektu S7Client całą operację pobrania danych ze sterownika wykonywana jest w metodzie
		 // steepPrepareRead() w sposób blokujący.
	 };
	/**
	 * Główne operacje wątku
	 */
	abstract public void steep();
	/**
	 * Zapisywanie zmodyfikowanych zmiennych do sterownika
	 */
	public void steepWrite(){

	};
	abstract public void steepException(Exception e);
	abstract public void steepExceptionFinally(Exception e);
	abstract public void steepFinaly();
	/**
	 * Metoda wywoływana na zakończenie kroku w celu wykonania wstępnego przygotowania do następnego kroku, by właściwy
	 * kod następnego kroku wykonał się jak najszybciej po uruchomieniu wątku.
	 */
	public void steepPrepareNextSteep(){};

	@Override
	public void run(){
		System.out.println("Process.run");
		try{
			steepPrepareRead();
			steepWaitRead();
			steepRead();
			steep();
			steepWrite();
			steepPrepareNextSteep();
		}catch(Exception e){
			try{
				steepException(e);
			}catch(Exception eBis){
				steepExceptionFinally(e);
				System.out.println("Process.run nie udana próba obsłużenia wyjątku Exception eBis");
			}
		}finally{
			steepFinaly();
		}
	}


	abstract protected void initMemory();
	abstract protected void initPlcs();
	protected void addPlc(PLC newPlc){
		plcs.put(Process1.id, newPlc);
	}


	//------------------------------------------------------------------------------

	protected byte[] d; // data : dane - pamięć "operacyjna"
	protected byte[] i; // input
	protected byte[] q; // quite

	protected MemoryD memoryD;
	protected MemoryI memoryI;
	protected MemoryQ memoryQ;
	// @todo opracować pozostałe bloki pamięci

	protected TreeMap<Integer, PLC> plcs;
	// Komórki pamięci zmienione podczas tego kroku. Będą wysłane do sterownika w metodzie steepWrite().
	protected TreeMap<Integer, MemoryCell> memDBChangedId;
	// Wyjścia zmienione podczas rego kroku. Będą wysłane do sterownika w metodzie steepWrite().
	protected TreeMap<Integer, MemoryCell> memOutChangedId;

}
