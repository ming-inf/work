package test.controlCode;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Joiner;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import junit.framework.TestCase;

public class ControlCodeTest extends TestCase {
	static final Character us = 0x31;
	static final Character rs = 0x30;
	static final Character gs = 0x29;
	static final Character fs = 0x28;
	static final Character stx = 0x02;

	static final Character usPicture = 0x241f;
	static final Character rsPicture = 0x241e;
	static final Character gsPicture = 0x241d;
	static final Character fsPicture = 0x241c;
	static final Character stxPicture = 0x2402;

	static final BiMap<Character, Character> code2picture = HashBiMap.create();
	{
		code2picture.put(us, usPicture);
		code2picture.put(rs, rsPicture);
		code2picture.put(gs, gsPicture);
		code2picture.put(fs, fsPicture);
		code2picture.put(stx, stxPicture);
	}
	static final BiMap<Character, Character> picture2code = code2picture.inverse();

	List<String> header = Arrays.asList("a", "b", "c");
	List<String> record1 = Arrays.asList("1", "2", "3");
	List<String> record2 = Arrays.asList("one", "two", "three");
	List<String> record3 = Arrays.asList("first", "second", "third");

	Joiner unitJoiner = Joiner.on(us);
	Joiner recordJoiner = Joiner.on(rs);
	Joiner headerJoiner = Joiner.on(stx);

	String head = unitJoiner.join(header);
	String rec1 = unitJoiner.join(record1);
	String rec2 = unitJoiner.join(record2);
	String rec3 = unitJoiner.join(record3);

	String records = recordJoiner.join(rec1, rec2, rec3);

	String group = headerJoiner.join(head, records);

	public static String toDisplay(String raw) {
		String result = raw;
		for (Map.Entry<Character, Character> e : code2picture.entrySet()) {
			result = StringUtils.replace(result, StringEscapeUtils.escapeJava(e.getKey().toString()), e.getValue().toString());
		}
		return result;
	}
	
	public static void print(String s) {
		System.out.println(s);
	}

	public void testApp() {
		print(group);
		print(toDisplay(group));
	}
}
