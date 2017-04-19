package satomaru.utility.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * コレクションの再構成をするのに Stream はちょっとノイズが多いよね、というツイートをみたので、コレクションの再構成をする為だけの軽量なツールを考えてみた。
 * 
 * @param <E> 再構成対象となるコレクションの要素の型
 * @param <C> 再構成対象となるコレクションの型
 * @param <R> Reassembler 派生クラスの型
 */
public abstract class Reassembler<E, C extends Collection<E>, R extends Reassembler<E, C, R>> {

	/**
	 * ArrayList の為の Reassembler。
	 * 
	 * @param <E> 再構成対象となるコレクションの要素の型
	 */
	public static final class ForArrayList<E> extends Reassembler<E, ArrayList<E>, ForArrayList<E>> {

		/**
		 * コンストラクタ。
		 * 
		 * @param stream 再構成対象となるコレクションの Stream
		 */
		private ForArrayList(Stream<E> stream) {
			super(stream, ArrayList::new, ForArrayList<E>::new);
		}

		/**
		 * 関数によって値をマッピングします。
		 * 
		 * @param mapper 値をマッピングする関数
		 * @return ArrayList の為の Reassembler
		 */
		public <F> ForArrayList<F> map(Function<? super E, F> mapper) {
			return new ForArrayList<>(stream.map(mapper));
		}
	}

	/**
	 * ArrayList の再構成を開始します。
	 * 
	 * @param arrayList 再構成対象となる ArrayList
	 * @return ArrayList の為の Reassembler
	 */
	public static <E> ForArrayList<E> of(ArrayList<E> arrayList) {
		return new ForArrayList<>(arrayList.stream());
	}

	/** 再構成対象となるコレクションの Stream。 */
	protected final Stream<E> stream;

	/** 再構成後のコレクションを生成する関数。 */
	protected final Supplier<C> collectionFactory;

	/** Reassembler を生成する関数。 */
	protected final Function<Stream<E>, R> reassemblerFactory;

	/**
	 * コンストラクタ。
	 * 
	 * @param stream 再構成対象となるコレクションの Stream
	 * @param collectionFactory 再構成後のコレクションを生成する関数
	 * @param reassemblerFactory Reassembler を生成する関数
	 */
	private Reassembler(
			Stream<E> stream,
			Supplier<C> collectionFactory,
			Function<Stream<E>, R> reassemblerFactory) {

		this.stream = stream;
		this.collectionFactory = collectionFactory;
		this.reassemblerFactory = reassemblerFactory;
	}

	/**
	 * 要素を絞り組みます。
	 * 
	 * @param predicate 絞り込む要素を判定する関数
	 * @return Reassembler
	 */
	public final R filter(Predicate<? super E> predicate) {
		return reassemblerFactory.apply(stream.filter(predicate));
	}

	/**
	 * コレクションの再構成を終了します。
	 * 
	 * @return 再構成後のコレクション
	 */
	public final C end() {
		return stream.collect(Collectors.toCollection(collectionFactory));
	}
}
