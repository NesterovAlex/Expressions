package com.nesterov.expressions.dao;

import com.nesterov.expressions.dao.mapper.ExpressionRowMapper;
import com.nesterov.expressions.model.Expression;
import com.nesterov.expressions.service.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static java.util.Optional.empty;
import static java.util.Optional.of;

@Component
public class ExpressionDao {

    private static final Logger log = LoggerFactory.getLogger(ExpressionDao.class);

    private static final String SELECT_BY_RESULT = "SELECT * FROM expressions WHERE result = ?";
    private static final String SELECT_BY_RESULT_GREATER_THAN_VALUE = "SELECT * FROM expressions WHERE result > ?";
    private static final String SELECT_BY_RESULT_GREATER_LESS_VALUE = "SELECT * FROM expressions WHERE result < ?";
    private static final String SELECT_BY_EXPRESSION = "SELECT * FROM expressions WHERE expression = ?";
    private static final String SELECT_BY_ID = "SELECT * FROM expressions WHERE id = ?";
    private static final String INSERT = "INSERT INTO expressions (expression , result) values (?, ?)";
    private static final String UPDATE = "UPDATE expressions SET expression = ?, result = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM expressions WHERE id = ?";
    private static final String SELECT = "SELECT * FROM expressions";

    private ExpressionRowMapper expressionRowMapper;
    private JdbcTemplate jdbcTemplate;

    public ExpressionDao(JdbcTemplate template, ExpressionRowMapper expressionRowMapper) {
        this.jdbcTemplate = template;
        this.expressionRowMapper = expressionRowMapper;
    }

    public void create(Expression expression) {
        log.debug("Create {}", expression);
        final KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(INSERT, new String[]{"id"});
            statement.setString(1, expression.getExpression());
            statement.setDouble(2, expression.getResult());
            return statement;
        }, holder);
        expression.setId(holder.getKey().longValue());
    }

    public Optional<Expression> get(long id) {
        log.debug("Get expression by id={}", id);
        try {
            return of(jdbcTemplate.queryForObject(SELECT_BY_ID, new Object[]{id}, expressionRowMapper));
        } catch (EmptyResultDataAccessException e) {
            return empty();
        }
    }

    public void delete(long id) {
        log.debug("Delete expression by id={}", id);
        jdbcTemplate.update(DELETE, id);
    }

    public void update(Expression expression) {
        log.debug("Update expression {}", expression);
        jdbcTemplate.update(UPDATE, expression.getExpression(), expression.getResult(), expression.getId());
    }

    public List<Expression> findAll() {
        log.debug("Find all expressions");
        return jdbcTemplate.query(SELECT, expressionRowMapper);
    }

    public Optional<Expression> findByExpression(String expression) {
        log.debug("Find  by expression={}", expression);
        try {
            return of(
                    jdbcTemplate.queryForObject(SELECT_BY_EXPRESSION, new Object[]{expression}, expressionRowMapper));
        } catch (EmptyResultDataAccessException e) {
            return empty();
        }
    }

    public List<Expression> findByResult(double result) {
        log.debug("Find expressions by result={}", result);
        return jdbcTemplate.query(SELECT_BY_RESULT, new Object[]{result}, expressionRowMapper);
    }


    public List<Expression> findAllThatResultGreaterThanValue(double value) {
        log.debug("Find expressions by value={}", value);
        return jdbcTemplate.query(SELECT_BY_RESULT_GREATER_THAN_VALUE, new Object[]{value}, expressionRowMapper);
    }

    public List<Expression> findAllThatResultLessThanValue(double value) {
        log.debug("Find expressions by value={}", value);
        return jdbcTemplate.query(SELECT_BY_RESULT_GREATER_LESS_VALUE, new Object[]{value}, expressionRowMapper);
    }
}
