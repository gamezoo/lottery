package me.zohar;

import org.junit.Test;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class SpelTest {

	@Test
	public void test() {
		StandardEvaluationContext context = new StandardEvaluationContext();
		context.setVariable("dictTypeCode", "111");
		context.setVariable("dictItemCode", "222");
		
		ExpressionParser parser = new SpelExpressionParser();
		System.out.println(parser.parseExpression("#dictTypeCode + '_' +  #dictItemCode").getValue(context));
	}

}
