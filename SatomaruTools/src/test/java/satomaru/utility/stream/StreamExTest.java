package satomaru.utility.stream;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import satomaru.utility.iterator.Iterators;
import satomaru.utility.tools.Result;

/**
 * 拡張 Stream のテスト。
*/
public class StreamExTest {

	public interface Model {
		boolean isTarget(Integer value);
		Integer execute(Integer value);
	}

	/**
	 * DIっぽいことができる Stream って、どうですかねえ。
	 */
	@Test
	public void testWith() {
		InjectedStream<Model, Integer> stream = StreamEx.of(1, 2, 3, 4)
			.with(Model.class)
			.filter(Model::isTarget)
			.map(Model::execute);

		Model myModel = new Model() {
			@Override
			public boolean isTarget(Integer value) {
				return value % 2 == 0;
			}

			@Override
			public Integer execute(Integer value) {
				return value / 2;
			}
		};

		List<Integer> actual = stream.mapToEx(myModel).unwrap().collect(Collectors.toList());
		assertEquals(Arrays.asList(1, 2), actual);
	}

	/**
	 * 安西先生、例外にタフな Stream が欲しいです……。
	 */
	@Test
	public void testProcess() {
		List<Result<BigDecimal>> actual = StreamEx.of(3L, 1L, null)
			.process(BigDecimal::valueOf)
			.process(d -> d.divide(BigDecimal.valueOf(3)))
			.mapToEx()
			.unwrap()
			.collect(Collectors.toList());

		assertEquals(3, actual.size());

		Result<BigDecimal> result;

		// 3 ÷ 3 = 1
		result = actual.get(0);
		assertEquals(BigDecimal.valueOf(1L), result.value().get());
		assertFalse(result.exception().isPresent());
		assertTrue(result.isOk());
		assertFalse(result.isNg());

		// 1 ÷ 3 → エラー
		result = actual.get(1);
		assertFalse(result.value().isPresent());
		assertEquals(ArithmeticException.class, result.exception().get().getClass());
		assertFalse(result.isOk());
		assertTrue(result.isNg());

		// null → エラー
		result = actual.get(2);
		assertFalse(result.value().isPresent());
		assertEquals(NullPointerException.class, result.exception().get().getClass());
		assertFalse(result.isOk());
		assertTrue(result.isNg());
	}

	/**
	 * zip 的なやつを Java で作ろうとしたら、2つの Stream じゃなくて、2つの Iterator でやらないと、なんだか後ろめたいよね……。
	 */
	@Test
	public void testZip() {
		List<String> actual = StreamEx.zip(Iterators.counter(), Iterators.array("foo", "bar", "baz"))
			.mapToEx((i, value) -> String.format("%d : %s", i, value))
			.unwrap()
			.collect(Collectors.toList());

		assertEquals(Arrays.asList("1 : foo", "2 : bar", "3 : baz"), actual);
	}
}
