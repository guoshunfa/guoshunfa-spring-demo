package com.panda.utils;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.StringUtils;

/**
 * @ClassName SpelUtils
 * @Description Spel 表达式解析 工具类
 * @Author guoshunfa
 * @Date 2021/12/1 上午9:50
 * @Version 1.0
 **/
public class SpelUtils {

    private static ExpressionParser parser = new SpelExpressionParser();

    public static <T> T getValue(String spel, Class<T> clazz) {
        if (StringUtils.hasText(spel)) return null;
        if (clazz == null) {
            clazz = (Class<T>) Object.class;
        }

        Expression unlessExpression = parser.parseExpression(spel);

        T value = unlessExpression.getValue(clazz);
        return value;
    }

    public static Object getValue(String spel) {
        return getValue(spel, null);
    }
}

