package com.tele2.calculator.service;

import com.tele2.calculator.domain.CalculatorOperation;
import com.tele2.calculator.enums.Operations;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class CalculatorDefaultCacheService implements CalculatorCacheService {

    private final Logger logger = Logger.getLogger(CalculatorDefaultCacheService.class.getName());
    private final CalculatorService calculatorService;
    private final List<CalculatorOperation> operationHistory = new ArrayList<>();
    private final Map<String, CalculatorOperation> operationCache = new HashMap<>();

    @Autowired
    public CalculatorDefaultCacheService(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    @PostConstruct
    public void loadOperationHistoryMap() {
        List<CalculatorOperation> operations = calculatorService.loadOperationHistoryData();
        if (!CollectionUtils.isEmpty(operations)) {
            operationHistory.addAll(operations);
            operationHistory.forEach(this::appendAction);
        }
    }

    @Override
    public CalculatorOperation add(String id, BigDecimal augend, BigDecimal addend) {
        return performAction(augend, addend, Operations.ADD);
    }

    @Override
    public CalculatorOperation subtract(String id, BigDecimal minuent, BigDecimal subtrahend) {
        return performAction(minuent, subtrahend, Operations.SUBTRACT);
    }

    @Override
    public CalculatorOperation multiply(String id, BigDecimal multiplier, BigDecimal multiplicand) {
        return performAction(multiplier, multiplicand, Operations.MULTIPLY);
    }

    @Override
    public CalculatorOperation divide(String id, BigDecimal dividend, BigDecimal divisor) {
        return performAction(dividend, divisor, Operations.DIVIDE);
    }

    @Override
    public List<CalculatorOperation> history(String id) {
        return StringUtils.isBlank(id) ? operationHistory :
                operationHistory.stream().filter(o -> o.getId().equals(id)).collect(Collectors.toList());
    }

    private CalculatorOperation performAction(BigDecimal first, BigDecimal second, Operations o) {
        String key = generateKey(first, second, o.getAction());
        CalculatorOperation calculatorOperation = operationCache.get(key);
        if (Objects.isNull(calculatorOperation)) {
            logger.info("Cache Miss for " + key);
            BigDecimal result = o.apply(first, second);
            calculatorOperation = processResult(UUID.randomUUID().toString(), first, second, o, result);
            operationHistory.add(calculatorOperation);
            appendAction(calculatorOperation);
            calculatorService.persistOperation(calculatorOperation);
        }
        return calculatorOperation;
    }

    private void appendAction(CalculatorOperation o) {
        Operations action = o.getOperations();
        operationCache.put(generateKey(o.getFirstOperand(), o.getSecondOperand(), o.getOperations().getAction()), o);
        if (action.equals(Operations.ADD) || action.equals(Operations.MULTIPLY)) {
            operationCache.put(generateKey(o.getSecondOperand(), o.getFirstOperand(), action.getAction()), o);
        }
        logger.info("Cache refreshed.");
    }

    private String generateKey(BigDecimal first, BigDecimal second, String keySeparator) {
        return "#" + first.stripTrailingZeros() + keySeparator + second.stripTrailingZeros();
    }

    private CalculatorOperation processResult(String id, BigDecimal firstOperand, BigDecimal secondOperand,
                                              Operations operations, BigDecimal result) {
        String processId = StringUtils.isNotBlank(id) ? id : UUID.randomUUID().toString();
        return CalculatorOperation.builder().id(processId).firstOperand(firstOperand)
                .secondOperand(secondOperand).operations(operations).result(result).build();
    }
}
