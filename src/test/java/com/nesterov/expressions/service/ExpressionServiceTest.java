package com.nesterov.expressions.service;

import com.nesterov.expressions.dao.ExpressionDao;
import com.nesterov.expressions.model.Expression;
import com.nesterov.expressions.service.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import java.util.ArrayList;
import java.util.List;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ExpressionServiceTest {

    @Mock
    private ExpressionDao expressionDao;

    @InjectMocks
    private ExpressionService expressionService;

    @Test
    void givenSubjectId_whenFindByResult_thenFindTeachers() {
        double result = 1;
        List<Expression> expressions = new ArrayList<>();
        Expression expression = new Expression("4 + 5", 9.0);
        expressions.add(expression);
        when(expressionDao.findByResult(result)).thenReturn(expressions);

        expressionService.expressionDao.findByResult(result);

        verify(expressionDao).findByResult(result);
    }

    @Test
    void givenListOfExistsExpressions_whenGetAll_thenExpectedListOfExpressionsReturned() {
        List<Expression> expressions = new ArrayList<>();
        expressions.add(new Expression(1, "7 + 3", 10.0));
        expressions.add(new Expression(2, "9 / 3", 3.0));
        expressions.add(new Expression(3, "4 - 2", 2.0));
        given(expressionDao.findAll()).willReturn(expressions);

        List<Expression> actual = expressionService.getAll();

        assertEquals(expressions, actual);
    }

    @Test
    void givenExpression_whenGet_thenEqualExpressionReturned() {
        Expression expression = new Expression(5, "72/8",9);
        given(expressionDao.get(expression.getId())).willReturn(of(expression));

        Expression actual = expressionService.get(expression.getId());

        assertEquals(expression, actual);
    }

    @Test
    void givenEmptyOption_whenGet_thenNotPresentEntityExceptionThrown() {
        Expression expression = new Expression(3, "7*8", 56.0);
        given(expressionDao.get(expression.getId())).willReturn(empty());

        assertThrows(NotFoundException.class, () -> expressionService.get(expression.getId()));
    }

    @Test
    void givenExpressionId_whenDelete_thenDeleted() {
        Expression expression = new Expression(7, "7 - 7", 0.0);
        when(expressionDao.get(expression.getId())).thenReturn(of(expression));

        expressionService.delete(expression.getId());

        verify(expressionDao).delete(expression.getId());
    }

    @Test
    void givenOptionalEmpty_whenDelete_thenNotFoundExceptionThrown() {
        Expression expression = new Expression(4, "17 + 3", 20.0);
        when(expressionDao.get(expression.getId())).thenReturn(empty());

        assertThrows(NotFoundException.class, () -> expressionService.delete(expression.getId()));
    }

    @Test
    void givenExpressionResult_whenUpdate_thenUpdated() {
        Expression expression = new Expression(5, "43 - 3", 40.0);
        when(expressionDao.findByResult(expression.getResult())).thenReturn(List.of(expression));

        expressionService.update(expression);

        verify(expressionDao).update(expression);
    }

    @Test
    void givenResultOfNonExistingExpression_whenUpdate_thenUpdated() {
        Expression expression = new Expression(5, "33 / 3", 11.0);
        when(expressionDao.findByResult(expression.getResult())).thenReturn(List.of(expression));

        expressionService.update(expression);

        verify(expressionDao).update(expression);
    }


    @Test
    void givenNonExistingExpression_whenCreate_thenCreated() {
        Expression expression = new Expression(1, "100 / 2", 50.0);
        when(expressionDao.findByResult(expression.getResult())).thenReturn(List.of(expression));

        expressionService.create(expression);

        verify(expressionDao).create(expression);
    }

}