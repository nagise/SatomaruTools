package satomaru.utility.stream;

import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import satomaru.utility.iterator.FunctionIterator;
import satomaru.utility.tools.Pair;
import satomaru.utility.tools.Result;

/**
 * Java Stream API にこういう機能があったらいいなあ的な妄想を書きなぐりました。
 *
 * @param <T> 要素
 */
public interface StreamEx<T> {

	/**
	 * 通常の Stream にマッピングします。
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
	default <U> PairStream<T, U> pair(Function<? super T, U> mapper) {
		return new PairStreamImpl<>(unwrap().map(t -> new Pair<>(t, mapper.apply(t))));
	}

	/**
	 * 例外をサポートする拡張 Stream を作成します。
	 * 
	 * <p>
	 * 関数の結果がnullの場合は、NullPointerExceptionが発生した扱いになります。
	 * </p>
	 * 
	 * @param processor 各要素を処理する関数
	 * @return 例外をサポートする拡張 Stream
	 */
	default <R> ResultStream<R> process(Result.Processor<? super T, R> processor) {
		return new ResultStreamImpl<R>(unwrap().map(t -> Result.of(() -> processor.process(t))));
	}

	/**
	 * インスタンスを生成します。
	 * 
	 * @param values 値
	 * @return インスタンス
	 */
	@SafeVarargs
	static <T> StreamEx<T> of(T... values) {
		return of(Stream.of(values));
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
	 * @param collection コレクション
	 * @return インスタンス
	 */
	static <T> StreamEx<T> of(Collection<T> collection) {
		return of(collection.stream());
	}

	/**
	 * インスタンスを生成します。
	 * 
	 * @param iterator イテレーター
	 * @return インスタンス
	 */
	static <T> StreamEx<T> of(Iterator<T> iterator) {
		Spliterator<T> spliterator = Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED);
		return of(StreamSupport.stream(spliterator, false));
	}

	/**
	 * 2つのイテレーターから PairStream を作成します。
	 * 
	 * <p>
	 * どちらかのイテレーターが終了するか、または null を返却した時点で、この PairStream は終了します。
	 * </p>
	 * 
	 * @param first 1つ目のイテレーター
	 * @param second 2つ目のイテレーター
	 * @return PairStream
	 */
	public static <F, S> PairStream<F, S> zip(Iterator<F> first, Iterator<S> second) {
		return new PairStreamImpl<>(
				StreamSupport.stream(
						Spliterators.spliteratorUnknownSize(
								FunctionIterator.of(
										() -> (first.hasNext() && second.hasNext())
												? new Pair<>(first.next(), second.next())
												: null
								),
								Spliterator.ORDERED
						),
						false
				)
		);
	}

	/**
	 * インスタンスを生成します。
	 * 
	 * @param seed 最初の値
	 * @param function 前の値から、次の値を作成する関数
	 * @return インスタンス
	 */
	static <T> StreamEx<T> iterate(T seed, UnaryOperator<T> function) {
		return of(Stream.iterate(seed, function));
	}
}
