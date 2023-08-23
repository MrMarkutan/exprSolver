package util;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.ValidationResult;

import java.util.EmptyStackException;

public class ExpressionUtil {
    public static Expression build(String line) throws IllegalArgumentException, EmptyStackException {
        return new ExpressionBuilder(line)
                .variable("x")
                .build();

    }

    public static ValidationResult validate(Expression expression, Double root) {
        return expression.setVariable("x", root).validate();
    }

    public static ValidationResult validate(Expression expression) {
        return validate(expression, 1.0);
    }
}
