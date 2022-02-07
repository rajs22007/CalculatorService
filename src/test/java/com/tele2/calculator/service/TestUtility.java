package com.tele2.calculator.service;

import com.tele2.calculator.domain.CalculatorOperation;
import com.tele2.calculator.domain.CalculatorOperations;
import com.tele2.calculator.enums.Operations;

import java.math.BigDecimal;

public class TestUtility {
    public CalculatorOperation calculatorOperation() {
        CalculatorOperation operation = new CalculatorOperation();
        operation.setId("TEST_ID");
        operation.setFirstOperand(BigDecimal.ONE);
        operation.setSecondOperand(BigDecimal.TEN);
        operation.setOperations(Operations.MULTIPLY);
        operation.setResult(BigDecimal.TEN);

        return operation;
    }

    public CalculatorOperations calculatorOperations() {
        CalculatorOperations operations = new CalculatorOperations();
        operations.setId("TEST_ID");
        operations.setFirstOperand(BigDecimal.ONE);
        operations.setSecondOperand(BigDecimal.TEN);
        operations.setOperations(Operations.MULTIPLY);
        operations.setResult(BigDecimal.TEN);

        return operations;
    }
}
