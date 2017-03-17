package satomaru.utility.stream;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import satomaru.utility.tools.Pair;

/**
 * 2つの値を扱う拡張 Stream の実装です。
 *
 * @param <T> 1つ目の値
 * @param <U> 2つ目の値
 */
final class PairStreamImpl<T, U> implements PairStream<T, U> {

	/** 元となる Stream。 */
	private final Stream<Pair<T, U>> stream;

	/**
	 * コンストラクタ。
	 * 
	 * @param stream 元となる Stream
	 */
	public PairStreamImpl(Stream<Pair<T, U>> stream) {
		this.stream = stream;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Stream<Pair<T, U>> mapToPair() {
		return stream;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <X> Stream<X> map(BiFunction<T, U, X> mapper) {
		return stream.map(p -> p.compute(mapper));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <X> PairStream<X, U> map1(Function<T, X> mapper) {
		return new PairStreamImpl<>(stream.map(p -> p.map1(mapper)));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <X> PairStream<T, X> map2(Function<U, X> mapper) {
		return new PairStreamImpl<>(stream.map(p -> p.map2(mapper)));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PairStream<T, U> filter(BiPredicate<T, U> predicate) {
		return new PairStreamImpl<>(stream.filter(p -> p.test(predicate)));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PairStream<T, U> filter1(Predicate<T> predicate) {
		return new PairStreamImpl<>(stream.filter(p -> p.test1(predicate)));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PairStream<T, U> filter2(Predicate<U> predicate) {
		return new PairStreamImpl<>(stream.filter(p -> p.test2(predicate)));
	}
}
