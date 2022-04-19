package com.nesterov.expressions.dao;

import com.nesterov.expressions.model.Expression;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static java.lang.Double.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;

@WebAppConfiguration
@SpringJUnitConfig(TestConfig.class)
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ExpressionDaoTest {

    @Autowired
    private ExpressionDao expressionDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Order(1)
    void givenExpectedCountOfRowsInTable_whenCreate_thenEqualsCountOfRowsReturned() {
        int expected = countRowsInTable(jdbcTemplate, "expressions") + 1;

        expressionDao.create(new Expression("8 * -7", -56.0));

        int actual = countRowsInTable(jdbcTemplate, "expressions");
        assertEquals(expected, actual);
    }

    @Test
    @Order(2)
    void givenExistingExpression_whenFindByResult_thenExpectedExpressionReturned() {
        Expression expected = new Expression(1, "8 * -7", -56.0);

        List<Expression> actual = expressionDao.findByResult(expected.getResult());

        assertEquals(expected, actual.get(0));
    }


    @Test
    @Order(3)
    void givenExpectedExpression_whenGet_thenRelevantGExpressionReturned() {
        Expression expected = new Expression(1, "8 * -7", -56.0);

        assertEquals(expected, expressionDao.get(1).orElse(null));
    }

    @Test
    @Order(4)
    void givenExpression_whenUpdate_thenExpressionWithUpdatedResultReturned() {
        Expression expression = new Expression(1, "-7 -7", -14.0);

        expressionDao.update(expression);

        String actual = jdbcTemplate.queryForObject("SELECT result FROM expressions WHERE id = 1", String.class);
        assertEquals(expression.getResult(), parseDouble(actual));
    }

    @Test
    @Order(5)
    void givenExpectedCountRowsInTable_whenFindAll_thenEqualCountOfRowsReturned() {
        assertEquals(countRowsInTable(jdbcTemplate, "expressions"), expressionDao.findAll().size());
    }

    @Test
    @Order(5)
    void givenExpectedCountOfRowsInTable_whenDelete_thenEqualCountRowsReturned() {
        int expected = countRowsInTable(jdbcTemplate, "expressions") - 1;

        expressionDao.delete(1);

        int actual = countRowsInTable(jdbcTemplate, "expressions");
        assertEquals(expected, actual);
    }

    @Test
    @Order(6)
    void givenExpectedExpression_whenFindAllThatResultGreaterThanValue_thenExpectedExpressionReturned() {
        int expected = countRowsInTable(jdbcTemplate, "expressions") + 1;
        Expression expectedExpression = new Expression("8 * -7", -56.0);

        expressionDao.create(expectedExpression);

        List<Expression> expressionList = expressionDao.findAllThatResultGreaterThanValue(-57.0);

        int actual = countRowsInTable(jdbcTemplate, "expressions");
        assertEquals(expected, actual);
        assertEquals(expectedExpression, expressionList.get(0));
    }

    @Test
    @Order(6)
    void givenExpectedExpression_whenFindAllThatResultLessThanValue_thenExpectedExpressionReturned() {
        int expected = countRowsInTable(jdbcTemplate, "expressions") + 1;
        Expression expectedExpression = new Expression(3,"8 * -7", -56.0);

        expressionDao.create(expectedExpression);

        List<Expression> expressionList = expressionDao.findAllThatResultLessThanValue(57.0);

        int actual = countRowsInTable(jdbcTemplate, "expressions");
        assertEquals(expected, actual);
        assertEquals(expectedExpression.getExpression(), expressionList.get(0).getExpression());
    }


}