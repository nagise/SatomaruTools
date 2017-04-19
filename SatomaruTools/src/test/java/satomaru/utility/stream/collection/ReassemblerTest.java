package satomaru.utility.stream.collection;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import satomaru.utility.collection.Reassembler;

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
		ArrayList<Integer> arrayList = new ArrayList<>();
		Collections.addAll(arrayList, 10, 20, 30, 40);

		ArrayList<String> actual = Reassembler.of(arrayList)
				.filter(n -> n < 25)
				.map(Object::toString)
				.end();

		assertEquals(Arrays.asList("10", "20"), actual);
	}
}
