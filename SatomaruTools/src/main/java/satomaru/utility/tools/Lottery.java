package satomaru.utility.tools;

import java.util.Collection;
import java.util.Random;
import java.util.function.Supplier;

/**
 * 与えられた要素をランダムに選択する為のツールです。
 * 
 * @param <T> 要素
 */
public interface Lottery<T> {

	/**
	 * 与えられた要素をランダムに選択します。
	 * 
	 * @return 選択された要素
	 */
	T draw();

	/**
	 * インスタンスを生成します。
	 * 
	 * @param values 選択される要素
	 * @return インスタンス
	 */
	@SafeVarargs
	static <T> Lottery<T> of(T... values) {
		Random random = new Random();
		return () -> values[random.nextInt(values.length)];
	}

	/**
	 * インスタンスを生成します。
	 * 
	 * @param values 選択される要素が格納されたコレクション
	 * @return インスタンス
	 */
	static <T> Lottery<T> of(Collection<T> values) {
		@SuppressWarnings("unchecked")
		T[] array = (T[]) values.toArray();
		return of(array);
	}

	/**
	 * インスタンスを生成します。
	 * 
	 * @param type 選択される要素に使用する列挙型
	 * @return インスタンス
	 */
	static <T extends Enum<T>> Lottery<T> of(Class<T> type) {
		return of(type.getEnumConstants());
	}

	/**
	 * 関数を作成します。
	 * 
	 * @param values 選択される要素
	 * @return 関数
	 */
	@SafeVarargs
	static <T> Supplier<T> supplier(T... values) {
		return of(values)::draw;
	}

	/**
	 * 関数を作成します。
	 * 
	 * @param values 選択される要素が格納されたコレクション
	 * @return 関数
	 */
	static <T> Supplier<T> supplier(Collection<T> values) {
		return of(values)::draw;
	}

	/**
	 * 関数を作成します。
	 * 
	 * @param type 選択される要素に使用する列挙型
	 * @return 関数
	 */
	static <T extends Enum<T>> Supplier<T> supplier(Class<T> type) {
		return of(type)::draw;
	}
}
