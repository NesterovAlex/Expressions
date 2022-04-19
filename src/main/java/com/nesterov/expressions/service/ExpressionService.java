package com.nesterov.expressions.service;

import com.nesterov.expressions.dao.ExpressionDao;
import com.nesterov.expressions.model.Expression;
import com.nesterov.expressions.service.exceptions.NotFoundException;
import com.nesterov.expressions.service.exceptions.NotUniqueExpressionException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

@Component
public class ExpressionService {

    ExpressionDao expressionDao;

    public ExpressionService(ExpressionDao expressionDao) {
        this.expressionDao = expressionDao;
    }

    public void create(Expression expression) {
        verifyExpressionIsUnique(expression);
        expressionDao.create(expression);
    }

    public Expression get(long id) {
        String message = format("Expression with id = '%s' not found", id);
        return expressionDao.get(id).orElseThrow(() -> new NotFoundException(message));
    }

    public void delete(long id) {
        if (!expressionDao.get(id).isPresent()) {
            String message = format("Expression with id = '%s' not found", id);
            throw new NotFoundException(message);
        }
        expressionDao.delete(id);
    }

    public void update(Expression expression) {
        verifyExpressionIsUnique(expression);
        expressionDao.update(expression);

    }

    public List<Expression> getAll() {
        return expressionDao.findAll();
    }

    public List<Expression> findByResult(double result) {
        return expressionDao.findByResult(result);
    }

    public List<Expression> findAllThatResultGreaterThanValue(double value) {
        return expressionDao.findAllThatResultGreaterThanValue(value);
    }

    public List<Expression> findAllThatResultLessThanValue(double value) {
        return expressionDao.findAllThatResultLessThanValue(value);
    }

    private void verifyExpressionIsUnique(Expression expression) {
        Optional<Expression> optionalExpression = expressionDao.findByExpression(expression.getExpression());
        if (optionalExpression.isPresent() && optionalExpression.orElseGet(() -> new Expression()).getId() != expression.getId()) {
            String message = format("Not unique expression = '%s'", expression.getExpression());
            throw new NotUniqueExpressionException(message);
        }
    }
}
