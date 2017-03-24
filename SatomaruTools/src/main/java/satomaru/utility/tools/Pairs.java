package satomaru.utility.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * ペアに関するユーティリティ。
 */
public final class Pairs {

	private Pairs() {

	}

	/**
	 * 2つのイテレーターからペアのリストを作成します。
	 * 
	 * <p>
	 * どちらかのイテレーターが終了した時点で、ペアの作成を終了します。
	 * </p>
	 * 
	 * @param first ペアの1つ目の値に用いるイテレーター
	 * @param second ペアの2つ目の値に用いるイテレーター
	 * @return ペアのリスト
	 */
	public static <F, S> List<Pair<F, S>> zip(Iterator<? extends F> first, Iterator<? extends S> second) {
		ArrayList<Pair<F, S>> result = new ArrayList<>();

		while (first.hasNext() && second.hasNext()) {
			result.add(new Pair<F, S>(first.next(), second.next()));
		}

		return result;
	}

	/**
	 * 2つのイテレーブルからペアのリストを作成します。
	 * 
	 * <p>
	 * どちらかのイテレーブルが終了した時点で、ペアの作成を終了します。
	 * </p>
	 * 
	 * @param first ペアの1つ目の値に用いるイテレーブル
	 * @param second ペアの2つ目の値に用いるイテレーブル
	 * @return ペアのリスト
	 */
	public static <F, S> List<Pair<F, S>> zip(Iterable<? extends F> first, Iterable<? extends S> second) {
		return zip(first.iterator(), second.iterator());
	}

	/**
	 * 2つの配列からペアのリストを作成します。
	 * 
	 * <p>
	 * どちらかの配列が終了した時点で、ペアの作成を終了します。
	 * </p>
	 * 
	 * @param first ペアの1つ目の値に用いる配列
	 * @param second ペアの2つ目の値に用いる配列
	 * @return ペアのリスト
	 */
	public static <F, S> List<Pair<F, S>> zip(F[] first, S[] second) {
		return zip(Arrays.asList(first), Arrays.asList(second));
	}

	/**
	 * 1つ目の値に用いるイテレーターと、2つ目の値を作成する関数から、ペアのリストを作成します。
	 * 
	 * @param iterator 1つ目の値に用いるイテレーター
	 * @param mapper インデックスと1つ目の値を受け取り、2つ目の値を作成する関数
	 * @return ペアのリスト
	 */
	public static <F, S> List<Pair<F, S>> map(
			Iterator<? extends F> iterator,
			BiFunction<Integer, ? super F, ? extends S> mapper) {

		ArrayList<Pair<F, S>> result = new ArrayList<>();
		int index = 0;

		while (iterator.hasNext()) {
			F first = iterator.next();
			result.add(new Pair<>(first, mapper.apply(index++, first)));
		}

		return result;
	}

	/**
	 * 1つ目の値に用いるイテレーブルと、2つ目の値を作成する関数から、ペアのリストを作成します。
	 * 
	 * @param iterable 1つ目の値に用いるイテレーブル
	 * @param mapper インデックスと1つ目の値を受け取り、2つ目の値を作成する関数
	 * @return ペアのリスト
	 */
	public static <F, S> List<Pair<F, S>> map(
			Iterable<? extends F> iterable,
			BiFunction<Integer, ? super F, ? extends S> mapper) {

		return map(iterable.iterator(), mapper);
	}

	/**
	 * 1つ目の値に用いる配列と、2つ目の値を作成する関数から、ペアのリストを作成します。
	 * 
	 * @param array 1つ目の値に用いる配列
	 * @param mapper インデックスと1つ目の値を受け取り、2つ目の値を作成する関数
	 * @return ペアのリスト
	 */
	public static <F, S> List<Pair<F, S>> map(F[] array, BiFunction<Integer, ? super F, ? extends S> mapper) {
		return map(Arrays.asList(array), mapper);
	}

	/**
	 * 関数を用いて、ペアのリストを変換します。
	 * 
	 * @param pairs 変換するペアのリスト
	 * @param function ペアを変換する関数
	 * @return 変換されたリスト
	 */
	public static <F, S, X> List<X> compute(
			List<Pair<F, S>> pairs,
			BiFunction<? super F, ? super S, ? extends X> function) {

		return pairs.stream().map(p -> function.apply(p.getFirst(), p.getSecond())).collect(Collectors.toList());
	}

	/**
	 * ペアのリストから、1つ目の値のリストを作成します。
	 * 
	 * @param pairs ペアのリスト
	 * @return 1つ目の値のリスト
	 */
	public static <F> List<F> firsts(List<Pair<? extends F, ?>> pairs) {
		return pairs.stream().map(Pair::getFirst).collect(Collectors.toList());
	}

	/**
	 * ペアのリストから、2つ目の値のリストを作成します。
	 * 
	 * @param pairs ペアのリスト
	 * @return 2つ目の値のリスト
	 */
	public static <S> List<S> seconds(List<Pair<?, ? extends S>> pairs) {
		return pairs.stream().map(Pair::getSecond).collect(Collectors.toList());
	}
}
