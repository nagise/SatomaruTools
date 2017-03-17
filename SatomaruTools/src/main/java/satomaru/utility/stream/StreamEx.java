package satomaru.utility.stream;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import satomaru.utility.tools.Pair;

/**
 * Java Stream API にこういう機能があったらいいなあ的な妄想を書きなぐりました。
 *
 * @param <T> 要素
 */
public interface StreamEx<T> {

	/**
	 * 通常の Stream を取り出します。
	 * 
	 * @return 通常のStream
	 */
	Stream<T> unwrap();

	/**
	 * 処理部品を呼び出すことが可能な拡張 Stream を作成します。
	 * 
	 * @param processorType 呼び出したい処理部品の型
	 * @return 処理部品を呼び出すことが可能な拡張 Stream
	 */
	default <P> InjectedStream<P, T> with(Class<P> processorType) {
		return new InjectedStreamImpl<>(unwrap(), new AtomicReference<>());
	}

	/**
	 * 2つの値を扱う拡張 Stream を作成します。
	 * 
	 * @param mapper 現在の値から2つめの値を作成する関数
	 * @return 現在の値と、関数によって作成された値の、2つの値を扱う拡張 Stream
	 */
	default <U> PairStream<T, U> pair(Function<T, U> mapper) {
		return new PairStreamImpl<>(unwrap().map(t -> new Pair<>(t, mapper.apply(t))));
	}

	/**
	 * インスタンスを生成します。
	 * 
	 * @param stream 通常の Stream
	 * @return インスタンス
	 */
	static <T> StreamEx<T> of(Stream<T> stream) {
		return () -> stream;
	}

	/**
	 * インスタンスを生成します。
	 * 
	 * @param streamGetter 通常の Stream を取得する関数
	 * @return インスタンス
	 */
	static <T> StreamEx<T> of(Supplier<Stream<T>> streamGetter) {
		return streamGetter::get;
	}

	/**
	 * インスタンスを生成します。
	 * 
	 * @param values 通常の Stream を構成する要素
	 * @return インスタンス
	 */
	@SafeVarargs
	static <T> StreamEx<T> of(T... values) {
		return of(Stream.of(values));
	}
}
