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
	 * @param step 増加値
	 * @return イテレーター
	 */
	public static Iterator<Integer> counter(int start, int step) {
		return FunctionIterator.of(i -> i * step + start);
	}

	/**
	 * 1ずつ増加する整数カウンターのイテレーターを作成します。
	 * 
	 * @param start 開始値
	 * @return イテレーター
	 */
	public static Iterator<Integer> counter(int start) {
		return counter(start, 1);
	}

	/**
	 * 1から始まり、1ずつ増加する整数カウンターのイテレーターを作成します。
	 * 
	 * @return イテレーター
	 */
	public static Iterator<Integer> counter() {
		return counter(1, 1);
	}

	/**
	 * 配列のイテレーターを作成します。
	 * 
	 * <p>
	 * 配列中に null が存在する場合は、そこで終了します。
	 * </p>
	 * 
	 * @param values 配列
	 * @return イテレーター
	 */
	@SafeVarargs
	public static <E> Iterator<E> array(E... values) {
		return FunctionIterator.of(i -> (i < values.length) ? values[i] : null);
	}

	/**
	 * 配列を先頭から順番に使用し、最後まで使用したらまた先頭に戻るイテレーターを作成します。
	 * 
	 * <p>
	 * 配列中に null が存在する場合は、そこで終了します。
	 * </p>
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
