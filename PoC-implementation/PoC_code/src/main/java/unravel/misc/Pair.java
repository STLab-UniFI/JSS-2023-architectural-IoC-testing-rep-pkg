package unravel.misc;

public class Pair<A, B> {
	
	// attributes
	
	private A a;
	private B b;
	
	/**
	 * Default constructor
	 */
	Pair(A a, B b) {
		this.a = a;
		this.b = b;
	}
	
	// methods
	
	public A getA() {
		return a;
	} 
	
	public B getB() {
		return b;
	} 
} 
