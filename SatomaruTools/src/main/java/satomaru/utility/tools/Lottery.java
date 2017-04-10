package satomaru.utility.tools;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
	Optional<T> draw();

	/**
	 * ロッテリーを作成するビルダーです。
	 * 
	 * @param <T> ロッテリーで扱う要素
	 */
	class Builder<T> {

		/** ロッテリーで扱う要素。 */
		private final List<T> values;

		/**
		 * コンストラクタ。
		 * 
		 * @param values ロッテリーで扱う要素
		 */
		private Builder(T[] values) {
			this.values = Arrays.asList(values);
		}

		/**
		 * コンストラクタ。
		 * 
		 * @param values ロッテリーで扱う要素
		 */
		public Builder(Collection<T> values) {
			this.values = new ArrayList<>(values);
		}

		/**
		 * 標準的なロッテリーを作成します。
		 * 
		 * @return 標準的なロッテリー
		 */
		public Lottery<T> standard() {
			Random random = new Random();
			return () -> Optional.of(values.get(random.nextInt(values.size())));
		}

		/**
		 * ボックスタイプ（引いた要素はなくなる）のロッテリーを作成します。
		 * 
		 * @return ボックスタイプ（引いた要素はなくなる）のロッテリー
		 */
		public Lottery<T> box() {
			ArrayList<T> list = new ArrayList<>(values);
			Collections.shuffle(list);
			ArrayDeque<T> deque = new ArrayDeque<>(list);
			return () -> Optional.ofNullable(deque.pollLast());
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
		return new Builder<>(values);
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
