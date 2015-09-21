package pjpl.s7.type;

/**
 * @author pjanczura
 */
public class LReal extends Variable<java.lang.Double>{

	public LReal(java.lang.Double var) {
		super(var);
	}
	public double getSimple(){
		return var;
	}

}
