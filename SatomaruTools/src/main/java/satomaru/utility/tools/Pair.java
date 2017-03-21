package satomaru.utility.tools;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 2つの値をペアで扱います。
 *
 * @param <F> 1つ目の値
 * @param <S> 2つ目の値
 */
public final class Pair<F, S> {

	/** 1つ目の値。 */
	private final F first;

	/** 1つ目の値。 */
	private final S second;

	/**
	 * コンストラクタ。
	 * 
	 * @param first 1つ目の値
	 * @param second 2つ目の値
	 */
	public Pair(F first, S second) {
		this.first = first;
		this.second = second;
	}

	/**
	 * 1つ目の値を取得します。
	 * 
	 * @return 1つ目の値
	 */
	public F getFirst() {
		return first;
	}

	/**
	 * 2つ目の値を取得します。
	 * 
	 * @return 2つ目の値
	 */
	public S getSecond() {
		return second;
	}

	/**
	 * 2つの値を計算します。
	 * 
	 * @param function 計算する関数
	 * @return 計算結果
	 */
	public <X> X compute(BiFunction<? super F, ? super S, X> function) {
		return function.apply(first, second);
	}

	/**
	 * マッパー関数によって新しいペアを作成します。
	 * 
	 * @param mapper1 1つ目の値をマッピングする関数
	 * @param mapper2 2つ目の値をマッピングする関数
	 * @return 新しいペア
	 */
	public <X, Y> Pair<X, Y> map(
			Function<? super F, ? extends X> mapper1,
			Function<? super S, ? extends Y> mapper2) {

		return new Pair<>(mapper1.apply(first), mapper2.apply(second));
	}

	/**
	 * マッパー関数によって新しいペアを作成します。
	 * 
	 * <p>
	 * 2つ目の値はそのまま用います。
	 * </p>
	 * 
	 * @param mapper 1つ目の値をマッピングする関数
	 * @return 新しいペア
	 */
	public <X> Pair<X, S> map1(Function<? super F, ? extends X> mapper) {
		return new Pair<>(mapper.apply(first), second);
	}

	/**
	 * マッパー関数によって新しいペアを作成します。
	 * 
	 * <p>
	 * 1つ目の値はそのまま用います。
	 * </p>
	 * 
	 * @param mapper 2つ目の値をマッピングする関数
	 * @return 新しいペア
	 */
	public <X> Pair<F, X> map2(Function<? super S, ? extends X> mapper) {
		return new Pair<>(first, mapper.apply(second));
	}

	/**
	 * 2つの値を検査します。
	 * 
	 * @param predicate 検査する関数
	 * @return 検査に合格した場合はtrue
	 */
	public boolean test(BiPredicate<? super F, ? super S> predicate) {
		return predicate.test(first, second);
	}

	/**
	 * 1つ目の値を検査します。
	 * 
	 * @param predicate 検査する関数
	 * @return 検査に合格した場合はtrue
	 */
	public boolean test1(Predicate<? super F> predicate) {
		return predicate.test(first);
	}

	/**
	 * 2つ目の値を検査します。
	 * 
	 * @param predicate 検査する関数
	 * @return 検査に合格した場合はtrue
	 */
	public boolean test2(Predicate<? super S> predicate) {
		return predicate.test(second);
	}

	/**
	 * 文字列表現を取得します。
	 * 
	 * @return 文字列表現
	 */
	@Override
	public String toString() {
		return String.format("{%s, %s}", first, second);
	}
}
