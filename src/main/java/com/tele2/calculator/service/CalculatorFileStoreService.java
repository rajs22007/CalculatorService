package com.tele2.calculator.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.tele2.calculator.domain.CalculatorOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class CalculatorFileStoreService implements CalculatorService {
    Logger logger = Logger.getLogger(CalculatorFileStoreService.class.getName());
    private final ObjectMapper objectMapper;
    private final List<CalculatorOperation> operationHistory = new ArrayList<>();

    @Value("classpath:data/calculation-results.json")
    private Resource resultFileResource;

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
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<CalculatorOperation> loadOperationHistoryData() {
        return operationHistory;
    }

    @Override
    public void persistOperation(final CalculatorOperation calculatorOperation) {
        try {
            File resultFile = resultFileResource.getFile();
            operationHistory.add(calculatorOperation);
            objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
            objectMapper.writeValue(resultFile, operationHistory);
            logger.info("persisted to file");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
