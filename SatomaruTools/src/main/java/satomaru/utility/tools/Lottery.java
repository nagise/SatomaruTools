package satomaru.utility.tools;

import java.util.Collection;
import java.util.Random;

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
	 * ロッテリーを作成するビルダーです。
	 * 
	 * @param <T> ロッテリーで扱う要素
	 */
	class Builder<T> {

		/** ロッテリーで扱う要素。 */
		private final T[] values;

		/**
		 * コンストラクタ。
		 * 
		 * @param values ロッテリーで扱う要素
		 */
		public Builder(T[] values) {
			this.values = values;
		}

		/**
		 * 標準的なロッテリーを作成します。
		 * 
		 * @return 標準的なロッテリー
		 */
		public Lottery<T> standard() {
			Random random = new Random();
			return () -> values[random.nextInt(values.length)];
		}
	}

	/**
	 * ロッテリーを作成するビルダーを準備します。
	 * 
	 * @param values ロッテリーで扱う要素
	 * @return ビルダー
	 */
	@SafeVarargs
	static <T> Builder<T> build(T... values) {
		return new Builder<>(values);
	}

	/**
	 * ロッテリーを作成するビルダーを準備します。
	 * 
	 * @param values ロッテリーで扱う要素
	 * @return ビルダー
	 */
	static <T> Builder<T> build(Collection<T> values) {
		@SuppressWarnings("unchecked")
		T[] array = (T[]) values.toArray();
		return new Builder<>(array);
	}

	/**
	 * ロッテリーを作成するビルダーを準備します。
	 * 
	 * @param type ロッテリーで扱う列挙型
	 * @return ビルダー
	 */
	static <T extends Enum<T>> Builder<T> build(Class<T> type) {
		return new Builder<>(type.getEnumConstants());
	}
}
