package satomaru.utility.stream;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

/**
 * 処理部品を呼び出すことが可能な拡張 Stream です。
 *
 * @param <P> 呼び出したい処理部品
 * @param <T> 要素
 */
interface InjectedStream<P, T> {

	/**
	 * 処理部品を使って値をマッピングします。
	 * 
	 * @param <R> マッピング後の要素
	 * @param mapper 処理部品を使って値をマッピングする関数
	 * @return マッピングされた後の拡張 Stream
	 */
	<R> InjectedStream<P, R> map(BiFunction<P, ? super T, ? extends R> mapper);

	/**
	 * 処理部品を使って値をStreamに変換した後、フラットマッピングします。
	 * 
	 * @param <R> マッピング後の要素
	 * @param mapper 処理部品を使って値をStreamにマッピングする関数
	 * @return フラットマッピングされた後の拡張 Stream
	 */
	<R> InjectedStream<P, R> flatMap(BiFunction<P, ? super T, ? extends Stream<? extends R>> mapper);

	/**
	 * 処理部品を使って値をフィルタリングします。
	 * 
	 * @param predicate 処理部品を使って値をフィルタリングする関数
	 * @return フィルタリングされた後の拡張 Stream
	 */
	InjectedStream<P, T> filter(BiPredicate<P, ? super T> predicate);

	/**
	 * 処理部品のインスタンスを確定して、通常の Stream を返却します。
	 * 
	 * @param processor 処理部品のインスタンス
	 * @return 通常の Stream
	 */
	Stream<T> dependOn(P processor);
}
