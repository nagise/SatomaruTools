package satomaru.utility.stream;

import java.util.function.Consumer;
import java.util.stream.Stream;

import satomaru.utility.tools.Result;
import satomaru.utility.tools.Result.Processor;

/**
 * 例外をサポートする拡張 Stream です。
 * 
 * @param <T> 処理結果の型
 */
public interface ResultStream<T> {

	/**
	 * Result の Stream にマッピングします。
	 * 
	 * @return Result の Stream
	 */
	Stream<Result<T>> mapToResult();

	/**
	 * 例外が発生しなかった場合、処理結果をさらに処理します。
	 * 
	 * <p>
	 * 処理結果がnullの場合は、NullPointerExceptionが発生した扱いになります。
	 * </p>
	 * 
	 * @param processor 処理結果をさらに処理する関数
	 * @return 処理した後の新しい拡張 Stream
	 */
	<R> ResultStream<R> process(Processor<? super T, ? extends R> processor);

	/**
	 * 例外が発生しなかった場合、処理結果を評価します。
	 * 
	 * @param consumer 処理結果を評価する関数
	 * @return 拡張 Stream
	 */
	ResultStream<T> whenOk(Consumer<? super T> consumer);

	/**
	 * 例外が発生した場合、その例外を評価します。
	 * 
	 * @param consumer 例外を評価する関数
	 * @return 拡張 Stream
	 */
	ResultStream<T> whenNg(Consumer<? super Exception> consumer);

	/**
	 * 指定された例外、またはそのサブクラスの例外が発生した場合、その例外を評価します。
	 * 
	 * @param type 評価対象となる例外
	 * @param consumer 例外を評価する関数
	 * @return 拡張 Stream
	 */
	<E extends Exception> ResultStream<T> when(Class<E> type, Consumer<? super E> consumer);
}
