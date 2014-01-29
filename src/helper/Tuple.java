package helper;

public class Tuple<E1, E2> {
	private E1 e1;
	private E2 e2;
	public Tuple(E1 e1, E2 e2) {
		this.e1 = e1;
		this.e2 = e2;
	}
	
	public E1 getFirst() {
		return this.e1;
	}
	
	public E2 getSecond() {
		return this.e2;
	}

}
