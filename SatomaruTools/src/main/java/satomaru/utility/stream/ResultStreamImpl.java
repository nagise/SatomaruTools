package satomaru.utility.stream;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import satomaru.utility.tools.Result;
import satomaru.utility.tools.Result.Processor;

/**
 * 例外をサポートする拡張 Stream の実装です。
 * 
 * @param <T> 処理結果の型
 */
public final class ResultStreamImpl<T> implements ResultStream<T> {

	/** 元となる Stream。 */
	private final Stream<Result<T>> stream;

	/**
	 * コンストラクタ。
	 * 
	 * @param stream 元となる Stream
	 */
	public ResultStreamImpl(Stream<Result<T>> stream) {
		this.stream = stream;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public StreamEx<Result<T>> mapToEx() {
		return () -> stream;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <X> StreamEx<X> mapToEx(Function<Result<? super T>, X> mapper) {
		return () -> stream.map(mapper);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <R> ResultStream<R> process(Processor<? super T, ? extends R> processor) {
		return new ResultStreamImpl<>(stream.map(r -> r.process(processor)));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResultStream<T> whenOk(Consumer<? super T> consumer) {
		return new ResultStreamImpl<>(stream.peek(r -> r.whenOk(consumer)));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResultStream<T> whenNg(Consumer<? super Exception> consumer) {
		return new ResultStreamImpl<>(stream.peek(r -> r.whenNg(consumer)));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E extends Exception> ResultStream<T> when(Class<E> type, Consumer<? super E> consumer) {
		return new ResultStreamImpl<>(stream.peek(r -> r.when(type, consumer)));
	}
}
