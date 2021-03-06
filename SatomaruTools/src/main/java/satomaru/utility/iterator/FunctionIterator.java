package satomaru.utility.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 関数によるイテレーターです。
 *
 * @param <E> 要素の型
 */
public class FunctionIterator<E> implements Iterator<E> {

	/**
	 * インデックスと直前の値から次の値を作る関数を用いて、イテレーターを作成します。
	 * 
	 * @param function インデックスと直前の値から次の値を作る関数
	 * @param prev 1回目の時に用いる「直前の値」
	 * @return イテレーター
	 */
	public static <E> FunctionIterator<E> of(BiFunction<Integer, E, E> function, E prev) {
		return new FunctionIterator<>(function, prev);
	}

	/**
	 * インデックスと直前の値から次の値を作る関数を用いて、イテレーターを作成します。
	 * 
	 * <p>
	 * 1回目の時の「直前の値」は、nullになります。
	 * </p>
	 * 
	 * @param function インデックスと直前の値から次の値を作る関数
	 * @return イテレーター
	 */
	public static <E> FunctionIterator<E> of(BiFunction<Integer, E, E> function) {
		return of(function, null);
	}

	/**
	 * インデックスから次の値を作る関数を用いて、イテレーターを作成します。
	 * 
	 * @param function インデックスから次の値を作る関数
	 * @return イテレーター
	 */
	public static <E> FunctionIterator<E> of(Function<Integer, E> function) {
		return of((i, e) -> function.apply(i));
	}

	/**
	 * 引数なしの関数を用いて、イテレーターを作成します。
	 * 
	 * @param function 次の値を作る関数
	 * @return
	 */
	public static <E> FunctionIterator<E> of(Supplier<E> function) {
		return of((i, e) -> function.get());
	}

	/** 次の値を作る関数。 */
	private final BiFunction<Integer, E, E> function;

	/** 現在の値。 */
	private Optional<E> value;

	/** インデックス。 */
	private int index;

	/** 次の値が算出済の場合はtrue。 */
	private boolean calculated;

	/**
	 * コンストラクタ。
	 * 
	 * @param function インデックスと直前の値から次の値を作る関数
	 * @param prev 1回目の時に用いる「直前の値」
	 */
	private FunctionIterator(BiFunction<Integer, E, E> function, E prev) {
		this.function = function;
		this.value = Optional.ofNullable(prev);
	}

	/**
	 * 次の値が存在する（nullでない）ことを判定します。
	 * 
	 * @return 次の値がnullでない場合はtrue
	 */
	@Override
	public boolean hasNext() {
		calculate();
		return value.isPresent();
	}

	/**
	 * 次の値を取得します。
	 * 
	 * @return 次の値
	 */
	@Override
	public E next() {
		calculate();
		calculated = false;
		return value.orElseThrow(NoSuchElementException::new);
	}

	/**
	 * 次の値を算出します。
	 * 
	 * @return 次の値
	 */
	private void calculate() {
		if (calculated) {
			return;
		}

		value = Optional.ofNullable(function.apply(index++, value.orElse(null)));
		calculated = true;
	}
}
