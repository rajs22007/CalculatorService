package com.tele2.calculator.service;

import com.tele2.calculator.domain.CalculatorOperation;
import com.tele2.calculator.enums.Operations;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface CalculatorService {

    CalculatorOperation add(String id, BigDecimal augend, BigDecimal addend);

    CalculatorOperation subtract(String id, BigDecimal minuent, BigDecimal subtrahend);

    CalculatorOperation multiply(String id, BigDecimal multiplier, BigDecimal multiplicand);

    CalculatorOperation divide(String id, BigDecimal dividend, BigDecimal divisor);

    List<CalculatorOperation> history(String id);

    default CalculatorOperation processResult(String id, BigDecimal firstOperand, BigDecimal secondOperand,
                                              Operations operations, BigDecimal result) {
        String processId = StringUtils.isNotBlank(id) ? id : UUID.randomUUID().toString();
        return CalculatorOperation.builder().id(processId).firstOperand(firstOperand)
                .secondOperand(secondOperand).operations(operations).result(result).build();
    }
}
