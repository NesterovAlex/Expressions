package com.nesterov.expressions.controller;

import com.nesterov.expressions.model.Expression;
import com.nesterov.expressions.service.ExpressionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.nesterov.expressions.util.ExpressionUtil.*;

@Controller
public class ExpressionController {

    private final ExpressionService expressionService;

    public ExpressionController(ExpressionService service) {
        this.expressionService = service;
    }

    @GetMapping("/expressions")
    public String getAll(Model model) {
        List<Expression> expressions = expressionService.getAll();
        model.addAttribute("expressions", expressions);
        return "expressions";
    }

    @RequestMapping(value = "/expressions/edit/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Expression findById(@PathVariable("id") long id, Model model) {
        Expression expression = expressionService.get(id);
        model.addAttribute("expression", expression);

        return expression;
    }

    @RequestMapping(value = "/expressions/details/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String expressionDetailsById(@PathVariable("id") long id, Model model) {
        Expression expression = expressionService.get(id);
        model.addAttribute("expression", expression);
        return "details";
    }

    @RequestMapping(value = "/updateExpression", method = {RequestMethod.PUT, RequestMethod.GET})
    public String update(@ModelAttribute("expression") Expression expression, Model model) {
        model.addAttribute("expression", expression);
        expression.setResult(сalculateResult(expression));
        expressionService.update(expression);
        return "redirect:/expressions";
    }

    @RequestMapping(value = "/createExpression", method = RequestMethod.POST)
    public String addExpression(@ModelAttribute("expression") Expression expression, Model model) {
        model.addAttribute("expression", expression);
        expression.setResult(сalculateResult(expression));
        expressionService.create(expression);
        return "redirect:/expressions";
    }

    @RequestMapping(value = "/expression/delete/{id}")
    public String delete(@PathVariable("id") long id, Model model) {
        Expression expression = expressionService.get(id);
        model.addAttribute("expression", expression);
        expressionService.delete(expression.getId());
        return "redirect:/expressions";
    }
}
