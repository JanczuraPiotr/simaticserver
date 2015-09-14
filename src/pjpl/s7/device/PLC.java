package pjpl.s7.device;

import Moka7.S7;
import Moka7.S7Client;

abstract public class PLC extends S7Client{

	public PLC(String IPAddress, int Rack, int Slot){
		super();
		connectTo(IPAddress, Rack, Slot);
	}
	public void connectTo(String IPAddress, int Rack, int Slot){
		SetConnectionType(S7.OP);
		errCode = super.ConnectTo(IPAddress, Rack, Slot);
		System.out.println("Kod uruchonmienia sterownika na bramie " + errCode);

	}

	public int getSizeArea(int areaCode){
		int ret = 0;
		switch(areaCode){
			case S7.I:
				ret = getSizeAreaI();
				break;
			case S7.Q:
				ret = getSizeAreaQ();
				break;
			case S7.D:
				ret = getSizeAreaD();
				break;
		}
		return ret;
	}

	// @todo Może lepiej usunąć metody getSizeXX a rozmiar danych określać w klasach MemoryX
	abstract public int getSizeAreaD();
	abstract public int getSizeAreaI();
	abstract public int getSizeAreaQ();

	//@prace należy usunąć te metody a cały kod readxxx write przenieść do Process.
	abstract public void readAreaDb();
	abstract public void readAreaIn();
	abstract public void readAreaOut();
	abstract public void writeAreaDb();
	abstract public void writeAreaOut();

	protected int errCode = 0;
}
