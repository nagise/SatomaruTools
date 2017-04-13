package satomaru.utility.stream;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import satomaru.utility.iterator.Iterators;

/**
 * 拡張 Stream のテスト。
*/
public class StreamExTest {

	/**
	 * zip 的なやつを Java で作ろうとしたら、2つの Stream じゃなくて、2つの Iterator でやらないと、なんだか後ろめたいよね……。
	 */
	@Test
	public void testZip() {
		List<String> actual = StreamEx.zip(Iterators.counter(), Iterators.array("foo", "bar", "baz"))
			.map((i, value) -> String.format("%d : %s", i, value))
			.collect(Collectors.toList());

		assertEquals(Arrays.asList("1 : foo", "2 : bar", "3 : baz"), actual);
	}
}
