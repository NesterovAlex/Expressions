package com.nesterov.expressions.util;

import com.nesterov.expressions.model.Expression;
import com.nesterov.expressions.service.exceptions.ParenthesesMismatchException;
import com.nesterov.expressions.service.exceptions.UnexpectedCharacterException;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static com.nesterov.expressions.util.LexemeType.MINUS;
import static com.nesterov.expressions.util.LexemeType.NUMBER;

public class ExpressionUtil {

    private ExpressionUtil() {
    }

    private static final String MESSAGE = "Unexpected token: ";
    private static final String AT_POSITION = " at position: ";


    public static List<Lexeme> lexAnalyze(String expText) {
        ArrayList<Lexeme> lexemes = new ArrayList<>();
        int position = 0;
        while (position < expText.length()) {
            char simbol = expText.charAt(position);
            switch (simbol) {
                case '(':
                    lexemes.add(new Lexeme(LexemeType.LEFT_BRACKET, simbol));
                    position++;
                    continue;
                case ')':
                    lexemes.add(new Lexeme(LexemeType.RIGHT_BRACKET, simbol));
                    position++;
                    continue;
                case '+':
                    lexemes.add(new Lexeme(LexemeType.PLUS, simbol));
                    position++;
                    continue;
                case '-':
                    lexemes.add(new Lexeme(LexemeType.MINUS, simbol));
                    position++;
                    continue;
                case '*':
                    lexemes.add(new Lexeme(LexemeType.MULTIPLY, simbol));
                    position++;
                    continue;
                case '/':
                    lexemes.add(new Lexeme(LexemeType.DIVIDE, simbol));
                    position++;
                    continue;
                default:
                    if (simbol <= '9' && simbol >= '0' || simbol == '.') {
                        StringBuilder sb = new StringBuilder();
                        do {
                            sb.append(simbol);
                            position++;
                            if (position >= expText.length()) {
                                break;
                            }
                            simbol = expText.charAt(position);
                        } while (simbol <= '9' && simbol >= '0' || simbol == '.');
                        lexemes.add(new Lexeme(NUMBER, sb.toString()));
                    } else {
                        if (simbol != ' ') {
                            throw new UnexpectedCharacterException("Unexpected character: " + simbol);
                        }
                        position++;
                    }
            }
        }
        lexemes.add(new Lexeme(LexemeType.EOF, ""));
        return lexemes;
    }


    public static double expr(LexemeBuffer lexemes) {
        Lexeme lexeme = lexemes.next();
        if (lexeme.type == LexemeType.EOF) {
            return 0;
        } else {
            lexemes.back();
            return plusminus(lexemes);
        }
    }

    public static double plusminus(LexemeBuffer lexemes) {
        double value = multdiv(lexemes);
        while (true) {
            Lexeme lexeme = lexemes.next();
            switch (lexeme.type) {
                case PLUS:
                    value += multdiv(lexemes);
                    break;
                case MINUS:
                    value -= multdiv(lexemes);
                    break;
                case EOF:
                case RIGHT_BRACKET:
                    lexemes.back();
                    return value;
                default:
                    throw new UnexpectedCharacterException(MESSAGE + lexeme.value
                            + AT_POSITION + lexemes.getPosition());
            }
        }
    }

    public static double multdiv(LexemeBuffer lexemes) {
        double value = factor(lexemes);
        while (true) {
            Lexeme lexeme = lexemes.next();
            switch (lexeme.type) {
                case MULTIPLY:
                    value *= factor(lexemes);
                    break;
                case DIVIDE:
                    value /= factor(lexemes);
                    break;
                case EOF:
                case RIGHT_BRACKET:
                case PLUS:
                case MINUS:
                    lexemes.back();
                    return value;
                default:
                    throw new UnexpectedCharacterException(MESSAGE + lexeme.value
                            + AT_POSITION + lexemes.getPosition());
            }
        }
    }

    public static double factor(LexemeBuffer lexemes) {
        Lexeme lexeme = lexemes.next();
        switch (lexeme.type) {
            case NUMBER:
                return Double.parseDouble(lexeme.value);
            case LEFT_BRACKET:
                double value = plusminus(lexemes);
                lexeme = lexemes.next();
                if (lexeme.type != LexemeType.RIGHT_BRACKET) {
                    throw new UnexpectedCharacterException(MESSAGE + lexeme.value
                            + AT_POSITION + lexemes.getPosition());
                }
                return value;
            default:
                throw new UnexpectedCharacterException(MESSAGE + lexeme.value
                        + AT_POSITION + lexemes.getPosition());
        }
    }

    public static double сalculateResult(Expression expression) {
        if (!isParenthesisMatch(expression.getExpression())) {
            throw new ParenthesesMismatchException("Parentheses mismatch");
        } else {
            List<Lexeme> lexemes = lexAnalyze(expression.getExpression());
            LexemeBuffer lexemeBuffer = new LexemeBuffer(checkLexemesList(lexemes));
            return expr(lexemeBuffer);
        }
    }

    public static List<Lexeme> checkLexemesList(List<Lexeme> lexemes) {
        StringBuilder builder;
        for (int i = 0; i < lexemes.size() - 1; i++) {
            if (lexemes.get(0).type == MINUS && lexemes.get(1).type == NUMBER) {
                lexemes.remove(0);
                builder = new StringBuilder();
                lexemes.set(0, new Lexeme(NUMBER, builder.append("-").append(lexemes.get(0).value).toString()));
            } else if (lexemes.get(i).type != NUMBER && lexemes.get(i + 1).type == MINUS && lexemes.get(i + 2).type == NUMBER) {
                lexemes.remove(i + 1);
                builder = new StringBuilder();
                lexemes.set(i + 1, new Lexeme(NUMBER, builder.append("-").append(lexemes.get(i + 1).value).toString()));
            }
        }
        return lexemes;
    }

    private static boolean isParenthesisMatch(String expressionString) {
        Stack<Character> stack = new Stack<>();
        char character;
        for (int i = 0; i < expressionString.length(); i++) {
            character = expressionString.charAt(i);
            if (character == '(') {
                stack.push(character);
            } else if (character == ')') {
                if (stack.empty())
                    return false;
                else if (stack.peek() == '(')
                    stack.pop();
                else
                    return false;
            }
        }
        return stack.empty();
    }
}
