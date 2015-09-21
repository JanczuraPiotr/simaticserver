package pjpl.s7.type;

/**
 * @author Piotr Janczura <piotr@janczura.pl>
 */
public class Int extends Variable<java.lang.Short>{
	public Int(java.lang.Short var) {
		super(var);
	}
	public short getSimple(){
		return var;
	}
}
