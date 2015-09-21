package pjpl.s7.type;

/**
 * @author Piotr Janczura <piotr@janczura.pl>
 */
public class Real extends Variable<java.lang.Float>{
	public Real(java.lang.Float var) {
		super(var);
	}
	public float getSimple(){
		return var;
	}

}
