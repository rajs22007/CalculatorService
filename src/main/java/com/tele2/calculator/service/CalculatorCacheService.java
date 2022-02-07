package com.tele2.calculator.service;

import com.tele2.calculator.domain.CalculatorOperation;

import java.math.BigDecimal;
import java.util.List;

public interface CalculatorCacheService {

    CalculatorOperation add(String id, BigDecimal augend, BigDecimal addend);

    CalculatorOperation subtract(String id, BigDecimal minuent, BigDecimal subtrahend);

    CalculatorOperation multiply(String id, BigDecimal multiplier, BigDecimal multiplicand);

    CalculatorOperation divide(String id, BigDecimal dividend, BigDecimal divisor);

    List<CalculatorOperation> history(String id);

}
