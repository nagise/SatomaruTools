package satomaru.utility.tools;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * try-catchをオブジェクトとしてサポートします。
 * 
 * @param <T> 処理結果の型
 */
public final class Result<T> {

	/**
	 * 処理を行います。
	 * 
	 * @param <T> 処理結果の型
	 */
	public interface Invoker<T> {

		/**
		 * 処理を行います。
		 * 
		 * @return 処理結果
		 * @throws Exception 処理中に発生した例外
		 */
		T invoke() throws Exception;
	}

	/**
	 * 処理を行います。
	 *
	 * @param <T> 処理対象の型
	 * @param <R> 処理結果の型
	 */
	public interface Processor<T, R> {

		/**
		 * 処理を行います。
		 * 
		 * @param value 処理対象
		 * @return 処理結果
		 * @throws Exception 処理中に発生した例外
		 */
		R process(T value) throws Exception;
	}

	/**
	 * 処理を行い、リザルトのインスタンスを作成します。
	 * 
	 * <p>
	 * 処理結果がnullの場合は、NullPointerExceptionが発生した扱いになります。
	 * </p>
	 * 
	 * @param invoker 処理を行う関数
	 * @return リザルトのインスタンス
	 */
	public static <T> Result<T> of(Invoker<? extends T> invoker) {
		try {
			return new Result<>(Optional.of(invoker.invoke()), Optional.empty());
		} catch (Exception e) {
			return new Result<>(Optional.empty(), Optional.of(e));
		}
	}

	/** 実行結果。 */
	private final Optional<T> value;

	/** 発生した例外。 */
	private final Optional<Exception> exception;

	/**
	 * コンストラクタ。
	 * 
	 * @param value 実行結果
	 * @param exception 発生した例外
	 */
	private Result(Optional<T> value, Optional<Exception> exception) {
		this.value = value;
		this.exception = exception;
	}

	/**
	 * 例外が発生しなかった場合、処理結果をさらに処理します。
	 * 
	 * <p>
	 * 処理結果がnullの場合は、NullPointerExceptionが発生した扱いになります。
	 * </p>
	 * 
	 * @param processor 処理結果をさらに処理する関数
	 * @return 処理した後のインスタンス
	 */
	public <R> Result<R> process(Processor<? super T, ? extends R> processor) {
		return (value.isPresent())
				? of(() -> processor.process(value.get()))
				: new Result<>(Optional.empty(), exception);
	}

	/**
	 * 例外が発生しなかった場合、処理結果を評価します。
	 * 
	 * @param consumer 処理結果を評価する関数
	 * @return このインスタンス自身
	 */
	public Result<T> whenOk(Consumer<? super T> consumer) {
		value.ifPresent(consumer);
		return this;
	}

	/**
	 * 例外が発生した場合、その例外を評価します。
	 * 
	 * @param consumer 例外を評価する関数
	 * @return このインスタンス自身
	 */
	public Result<T> whenNg(Consumer<? super Exception> consumer) {
		exception.ifPresent(consumer);
		return this;
	}

	/**
	 * 指定された例外、またはそのサブクラスの例外が発生した場合、その例外を評価します。
	 * 
	 * @param type 評価対象となる例外
	 * @param consumer 例外を評価する関数
	 * @return このインスタンス自身
	 */
	public <E extends Exception> Result<T> when(Class<E> type, Consumer<? super E> consumer) {
		exception.filter(type::isInstance).ifPresent(e -> consumer.accept(type.cast(e)));
		return this;
	}

	/**
	 * 実行結果を取得します。
	 * 
	 * @return 実行結果
	 */
	public Optional<T> value() {
		return value;
	}

	/**
	 * 発生した例外を取得します。
	 * 
	 * @return 発生した例外
	 */
	public Optional<Exception> exception() {
		return exception;
	}

	/**
	 * 例外が発生しなかったことを判定します。
	 * 
	 * @return 例外が発生しなかった場合はtrue
	 */
	public boolean isOk() {
		return value.isPresent();
	}

	/**
	 * 例外が発生したことを判定します。
	 * 
	 * @return 例外が発生した場合はtrue
	 */
	public boolean isNg() {
		return exception.isPresent();
	}

	/**
	 * 例外が発生した場合はその例外を送出し、そうでない場合は実行結果を返却します。
	 * 
	 * @return 実行結果
	 * @throws Exception 例外が発生した場合
	 */
	public T unwrap() throws Exception {
		return value.orElseThrow(exception::get);
	}

	/**
	 * 文字列表現を取得します。
	 * 
	 * @return 文字列表現
	 */
	@Override
	public String toString() {
		return value.map(Object::toString).orElseGet(() -> exception.map(Object::toString).get());
	}
}
