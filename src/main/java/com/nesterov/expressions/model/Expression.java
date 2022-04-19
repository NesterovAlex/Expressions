package com.nesterov.expressions.model;

import java.util.Objects;

public class Expression {

    private long id;
    private String expression;
    private double result;

    public Expression() {
    }

    public Expression(String expression) {
        this.expression = expression;
    }

    public Expression(String expression, double result) {
        this.expression = expression;
        this.result = result;
    }

    public Expression(long id, String expression, double result) {
        this.id = id;
        this.expression = expression;
        this.result = result;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public Double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Expression that = (Expression) o;
        return id == that.id && Double.compare(that.result, result) == 0 && expression.equals(that.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, expression, result);


    }

    @Override
    public String toString() {
        return "Expression{" +
                "id=" + id +
                ", expression='" + expression + '\'' +
                ", result=" + result +
                '}';
    }

}
