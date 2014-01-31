package helper;

public class Helper {
	public static <E> void shuffle(E[] elms) {
		E tmp;
		for (int i=0; i < elms.length; i++) {
			int swap = ((int)(Math.random()*(elms.length - i))) + i; // random from [i, cards.length)
			tmp = elms[swap];
			elms[swap] = elms[i];
			elms[i] = tmp;
		}
	}
}

