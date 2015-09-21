package pjpl.s7.type;
/**
 * @author Piotr Janczura
 */
public class Byte extends Variable<java.lang.Byte>{
	public Byte(java.lang.Byte var){
		super(var);
	}
	public byte getSimple(){
		return var;
	}
}
