package satomaru.utility.tools;

import java.util.Comparator;
import java.util.function.Function;

/**
 * 簡単なコンパレータです。
 *
 * @param <T> 比較対象
 * @param <E> 実際に比較する値の型
 */
public class Sort<T, E extends Comparable<E>> implements Comparator<T> {

	/** 比較対象から実際に比較する値を取得する関数。 */
	private final Function<T, E> comparableGetter;

	/**
	 * コンストラクタ。
	 * 
	 * @param comparableGetter 比較対象から実際に比較する値を取得する関数
	 */
	public Sort(Function<T, E> comparableGetter) {
		this.comparableGetter = comparableGetter;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compare(T t1, T t2) {
		E e1 = comparableGetter.apply(t1);
		E e2 = comparableGetter.apply(t2);
		return e1.compareTo(e2);
	}
}
