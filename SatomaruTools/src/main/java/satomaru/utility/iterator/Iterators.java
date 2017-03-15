package satomaru.utility.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * イテレーターに関するユーティリティです。
 */
public final class Iterators {

	private Iterators() {

	}

	/**
	 * 整数カウンターのイテレーターを作成します。
	 * 
	 * @param start 開始値
	 * @return イテレーター
	 */
	public static Iterator<Integer> counter(int start) {
		return FunctionIterator.of(i -> i + start);
	}

	/**
	 * 配列を先頭から順番に使用し、最後まで使用したらまた先頭に戻るイテレーターを作成します。
	 * 
	 * 
	 * @param values 使用する配列
	 * @return イテレーター
	 */
	@SafeVarargs
	public static <E> Iterator<E> rotator(E... values) {
		if (values.length == 0) {
			throw new NoSuchElementException();
		}

		return FunctionIterator.of(i -> values[i % values.length]);
	}
}
