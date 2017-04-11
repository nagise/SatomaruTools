package satomaru.utility.tools;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import satomaru.utility.stream.StreamEx;

/**
 * try-catch をサポートした拡張 Stream のテストケース。
 * 
 * <p>
 * 安西先生、例外にタフな Stream が欲しいです……。
 * </p>
 */
public class ResultStreamTest {

	@Test
	public void test() throws Exception {
		List<Result<BigDecimal>> actual = StreamEx.of(3L, 1L, null)
			.process(BigDecimal::valueOf)
			.process(d -> d.divide(BigDecimal.valueOf(3)))
			.mapToResult()
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
}
