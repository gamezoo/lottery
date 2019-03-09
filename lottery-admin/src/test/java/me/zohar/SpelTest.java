package me.zohar;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class SpelTest {

	@Test
	public void test() {
		Set<String> dictItemCodeSet = new HashSet<String>();
		System.out.println(dictItemCodeSet.add("1"));
		System.out.println(dictItemCodeSet.add("1"));
	}

}
