package com.boot.redis.config.util;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * @package : com.boot.redis.config.util
 * @name : CustomSpringElParser.java
 * @date : 2025. 4. 15. 오후 12:45
 * @author : lucaskang(swings134man)
 * @Description: Spring Expression Language (SpEL) Parser
**/
public class CustomSpringElParser {
    public CustomSpringElParser() {
    }

    public static Object getDynamicValue(String[] parameterNames, Object[] args, String key) {
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();

        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }

        return parser.parseExpression(key).getValue(context, Object.class);
    }
}
