package pjpl.s7.type;

/**
 * @author Piotr Janczura <piotr@janczura.pl>
 */
public class DInt extends Variable<java.lang.Integer>{
	public DInt(java.lang.Integer var){
		super(var);
	}
	public int getSimple(){
		return var;
	}
}
