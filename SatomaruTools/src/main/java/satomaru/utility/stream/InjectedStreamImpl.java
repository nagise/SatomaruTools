package satomaru.utility.stream;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

/**
 * 処理部品を呼び出すことが可能な拡張 Stream の実装です。
 *
 * @param <P> 呼び出したい処理部品
 * @param <T> 要素
 */
public final class InjectedStreamImpl<P, T> implements InjectedStream<P, T> {

	/** 処理を委譲する Stream。 */
	private final Stream<T> stream;

	/** 処理部品のインスタンス。 */
	private final AtomicReference<P> processor;

	/**
	 * コンストラクタ。
	 * 
	 * @param stream 処理を委譲する Stream
	 */
	public InjectedStreamImpl(Stream<T> stream, AtomicReference<P> processor) {
		this.stream = stream;
		this.processor = processor;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <R> InjectedStream<P, R> map(BiFunction<P, ? super T, ? extends R> mapper) {
		return new InjectedStreamImpl<>(stream.map(t -> mapper.apply(processor.get(), t)), processor);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <R> InjectedStream<P, R> flatMap(BiFunction<P, ? super T, ? extends Stream<? extends R>> mapper) {
		return new InjectedStreamImpl<>(stream.flatMap(t -> mapper.apply(processor.get(), t)), processor);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InjectedStream<P, T> filter(BiPredicate<P, ? super T> predicate) {
		return new InjectedStreamImpl<>(stream.filter(t -> predicate.test(processor.get(), t)), processor);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public StreamEx<T> mapToEx(P processor) {
		this.processor.set(processor);
		return () -> stream;
	}
}
