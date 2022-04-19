package com.nesterov.expressions.util;

import com.nesterov.expressions.model.Expression;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ExpressionUtilTest {

    @Test
    void givenExpression_whenCalculateResult_thenExpectedResultReturned(){
        Expression expression = new Expression("122.22 - 34 - 3* (55 + 5* (3 - 2)) * 2");

        assertEquals(-271.78, ExpressionUtil.сalculateResult(expression));
    }

    @Test
    void givenExpressionWithParenthesesMismatch_whenCalculateResult_thenExpectedResultReturned(){
        Expression expression = new Expression("122.22 - 34 - 3* (55 + 5* (3 - 2) * 2");

        assertThrows(RuntimeException.class, () -> ExpressionUtil.сalculateResult(expression));
    }

    @Test
    void givenExpressionWithParenthesesMismatch_whenCalculateResult_thenExceptionWithExpectedMassageReturned(){
        Expression expression = new Expression(" ( 122.22 - 34");

        Exception exception = assertThrows(RuntimeException.class, () -> ExpressionUtil.сalculateResult(expression));

        String expectedMessage = "Parentheses mismatch";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void givenAnExpressionWithTwoInvalidMathematicalCharactersInARow_whenCalculateResult_thenExceptionWithExpectedMassageReturned(){
        Expression expression = new Expression(" 40.3 + * 4");

        Exception exception = assertThrows(RuntimeException.class, () -> ExpressionUtil.сalculateResult(expression));

        String expectedMessage = "Unexpected token: ";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void givenAnExpressionWithPlusAndDivisionSignsInARow_whenCalculateResult_thenExceptionWithExpectedMassageReturned(){
        Expression expression = new Expression(" 3.6 + / 54");

        Exception exception = assertThrows(RuntimeException.class, () -> ExpressionUtil.сalculateResult(expression));

        String expectedMessage = "Unexpected token: ";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void givenAnExpressionWithDivisionAndMultiplicationSignsInARow_whenCalculateResult_thenExceptionWithExpectedMassageReturned(){
        Expression expression = new Expression(" 89 * / 4");

        Exception exception = assertThrows(RuntimeException.class, () -> ExpressionUtil.сalculateResult(expression));

        String expectedMessage = "Unexpected token: ";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void givenAnExpressionWithDivisionAndPlusSignsARow_whenCalculateResult_thenExceptionWithExpectedMassageReturned(){
        Expression expression = new Expression(" 3 / + 4");

        Exception exception = assertThrows(RuntimeException.class, () -> ExpressionUtil.сalculateResult(expression));

        String expectedMessage = "Unexpected token: ";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void givenAnExpressionWithDivisionAndMinusSignsARow_whenCalculateResult_thenExpectedResultReturned(){
        Expression expression = new Expression(" 7 / - 2");

        assertEquals(-3.5, ExpressionUtil.сalculateResult(expression));
    }

    @Test
    void givenAnExpressionWithPlusAndMinusSignsARow_whenCalculateResult_thenExpectedResultReturned(){
        Expression expression = new Expression(" 9 + - 2");

        assertEquals(7, ExpressionUtil.сalculateResult(expression));
    }

    @Test
    void givenAnExpressionWithMinusAndMinusSignsARow_whenCalculateResult_thenExpectedResultReturned(){
        Expression expression = new Expression(" 9 - - 2.5");

        assertEquals(11.5, ExpressionUtil.сalculateResult(expression));
    }

    @Test
    void givenAnExpressionWithTwoNegativeNumbersAndMinusSign_whenCalculateResult_thenExpectedResultReturned(){
        Expression expression = new Expression(" -9.5 - - 2.5");

        assertEquals(-7.0, ExpressionUtil.сalculateResult(expression));
    }

    @Test
    void givenAnExpressionWithMinusAndDivisionSignsARow_whenCalculateResult_thenExceptionWithExpectedMassageReturned(){
        Expression expression = new Expression(" 9 - / 2.5");

        Exception exception = assertThrows(RuntimeException.class, () -> ExpressionUtil.сalculateResult(expression));

        String expectedMessage = "Unexpected token: ";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void givenAnExpressionWithMinusAtTheBeginning_whenCalculateResult_thenExpectedNegativeResultReturned(){
        Expression expression = new Expression("- 4 * 7");

        assertEquals(-28, ExpressionUtil.сalculateResult(expression));
    }

    @Test
    void givenAnExpressionWithMultiplyAndMinusOperatorsInARow_whenCalculateResult_thenExpectedNegativeResultReturned(){
        Expression expression = new Expression("4 *- 7");

        assertEquals(-28, ExpressionUtil.сalculateResult(expression));
    }

    @Test
    void givenAnExpressionWithMultiplyAndMinusOperatorsInARow_whenCalculateResult_thenExpectedNegativeResultReturne(){
        Expression expression = new Expression("4 *- 7");

        assertEquals(-28, ExpressionUtil.сalculateResult(expression));
    }
}