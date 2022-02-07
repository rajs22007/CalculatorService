package com.tele2.calculator.service;

import com.tele2.calculator.domain.CalculatorOperation;

import java.util.List;

public interface CalculatorService {

    List<CalculatorOperation> loadOperationHistoryData();

    void persistOperation(CalculatorOperation calculatorOperation);
}
