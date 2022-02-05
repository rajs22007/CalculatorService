package com.tele2.calculator.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.tele2.calculator.domain.CalculatorOperation;
import com.tele2.calculator.enums.Operations;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class CalculatorFileStoreService implements CalculatorService {
    Logger logger = Logger.getLogger(CalculatorFileStoreService.class.getName());
    private final ObjectMapper objectMapper;
    private final List<CalculatorOperation> operationHistory = new ArrayList<>();
    private final Map<String, CalculatorOperation> operationCache = new HashMap<>();

    @Value("classpath:data/calculation-results.json")
    private Resource resultFileResource;

    @Autowired
    public CalculatorFileStoreService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void loadOperationHistory() {
        List<CalculatorOperation> operations;
        try {
            File resultFile = resultFileResource.getFile();
            objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
            objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT);
            operations = objectMapper.readValue(new FileReader(resultFile),
                    new TypeReference<List<CalculatorOperation>>() {
                    });
            if (!CollectionUtils.isEmpty(operations)) {
                operationHistory.addAll(operations);
                loadOperationHistoryMap();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadOperationHistoryMap() {
        operationHistory.forEach(this::appendAction);
    }

    private String generateKey(BigDecimal first, BigDecimal second, String keySeparator) {
        return "#" + first.stripTrailingZeros() + keySeparator + second.stripTrailingZeros();
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
            appendAction(calculatorOperation);
            persistOperation(calculatorOperation);
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

    private void persistOperation(final CalculatorOperation calculatorOperation) {
        try {
            File resultFile = resultFileResource.getFile();
            operationHistory.add(calculatorOperation);
            objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
            //objectMapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
            objectMapper.writeValue(resultFile, operationHistory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
