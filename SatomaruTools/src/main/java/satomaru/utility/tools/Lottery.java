package satomaru.utility.tools;

import java.util.Random;

public interface Lottery<T> {

	T draw();

	@SafeVarargs
	static <T> Lottery<T> of(T... values) {
		Random random = new Random();
		return () -> values[random.nextInt(values.length)];
	}
}
