package pjpl.s7.type;

/**
 * @author Piotr Janczura <piotr@janczura.pl>
 * @param <T>
 */
public abstract class Variable<T> {
	public Variable(T var){
		this.var = var;
	}
	public T get(){
		return var;
	};
	public void set(T var){
		this.var = var;
	}
	T var;
}