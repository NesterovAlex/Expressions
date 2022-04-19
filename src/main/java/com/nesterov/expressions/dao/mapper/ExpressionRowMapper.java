package com.nesterov.expressions.dao.mapper;

import com.nesterov.expressions.model.Expression;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ExpressionRowMapper implements RowMapper<Expression> {

    @Override
    public Expression mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Expression expression = new Expression();
        expression.setId(resultSet.getLong("id"));
        expression.setExpression(resultSet.getString("expression"));
        expression.setResult(resultSet.getDouble("result"));
        return expression;
    }
}
