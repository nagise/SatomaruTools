package satomaru.utility.stream;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import satomaru.utility.tools.Pair;

/**
 * 2つの値を扱う拡張 Stream です。
 *
 * @param <T> 1つ目の値
 * @param <U> 2つ目の値
 */
public interface PairStream<T, U> {

	/**
	 * ペアの Stream にマッピングします。
	 * 
	 * @return ペアの Stream
	 */
	Stream<Pair<T, U>> mapToPair();

	/**
	 * 2つの値から新しい Stream にマッピングします。
	 * 
	 * @param mapper 2つの値をマッピングする関数
	 * @return マッピングされた新しい Stream
	 */
	<X> Stream<X> map(BiFunction<T, U, X> mapper);

	/**
	 * 1つ目の値だけが別の型にマッピングされた、新しい拡張 Stream を作成します。
	 * 
	 * @param mapper 1つ目の値をマッピングする関数
	 * @return マッピングされた新しい拡張 Stream
	 */
	<X> PairStream<X, U> map1(Function<T, X> mapper);

	/**
	 * 2つ目の値だけが別の型にマッピングされた、新しい拡張 Stream を作成します。
	 * 
	 * @param mapper 2つ目の値をマッピングする関数
	 * @return マッピングされた新しい拡張 Stream
	 */
	<X> PairStream<T, X> map2(Function<U, X> mapper);

	/**
	 * 2つの値を元にフィルタリングします。
	 * 
	 * @param predicate 2つの値を元にフィルタリングする関数
	 * @return フィルタリングされた後の拡張 Stream
	 */
	PairStream<T, U> filter(BiPredicate<T, U> predicate);

	/**
	 * 1つ目の値を元にフィルタリングします。
	 * 
	 * @param predicate 1つ目の値を元にフィルタリングする関数
	 * @return フィルタリングされた後の拡張 Stream
	 */
	PairStream<T, U> filter1(Predicate<T> predicate);

	/**
	 * 2つ目の値を元にフィルタリングします。
	 * 
	 * @param predicate 2つ目の値を元にフィルタリングする関数
	 * @return フィルタリングされた後の拡張 Stream
	 */
	PairStream<T, U> filter2(Predicate<U> predicate);
}
