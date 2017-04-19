package satomaru.utility.collection;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import org.junit.Test;

/**
 * コレクションの再構成をするのに Stream はちょっとノイズが多いよね、というツイートをみたので、コレクションの再構成をする為だけの軽量なツールを考えてみた。
 * 
 * <p>
 * ……けど、なんか微妙……。
 * </p>
 */
public class ReassemblerTest {

	/**
	 * ArrayList の再構成を行うテスト。
	 */
	@Test
	public void testForArrayList() {
		ArrayList<Integer> original = new ArrayList<>();
		Collections.addAll(original, 10, 20, 30, 40);

		ArrayList<String> actual = Reassembler.of(original)
				.filter(n -> n < 25)
				.map(Object::toString)
				.end();

		assertEquals(Arrays.asList("10", "20"), actual);
	}

	/**
	 * HashSet の再構成を行うテスト。
	 */
	@Test
	public void testForHashSet() {
		HashSet<Integer> original = new HashSet<>();
		Collections.addAll(original, 10, 20, 30, 40);

		HashSet<String> actual = Reassembler.of(original)
				.filter(n -> n < 25)
				.map(Object::toString)
				.end();

		assertEquals(new HashSet<>(Arrays.asList("10", "20")), actual);
	}
}
