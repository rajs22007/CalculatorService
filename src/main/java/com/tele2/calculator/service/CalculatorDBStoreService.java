package com.tele2.calculator.service;

import com.tele2.calculator.domain.CalculatorOperation;
import com.tele2.calculator.domain.CalculatorOperations;
import com.tele2.calculator.repository.CalculatorRepository;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class CalculatorDBStoreService implements CalculatorService {
    Logger logger = Logger.getLogger(CalculatorDBStoreService.class.getName());
    private final CalculatorRepository calculatorRepository;

    public CalculatorDBStoreService(CalculatorRepository calculatorRepository) {
        this.calculatorRepository = calculatorRepository;
    }

    @Override
    public List<CalculatorOperation> loadOperationHistoryData() {
        List<CalculatorOperations> operations = calculatorRepository.findAll();
        return operations.stream().map(o -> {
            CalculatorOperation operation = new CalculatorOperation();
            BeanUtils.copyProperties(o, operation);
            return operation;
        }).collect(Collectors.toList());
    }

    @Override
    public void persistOperation(CalculatorOperation calculatorOperation) {
        CalculatorOperations operations = new CalculatorOperations();
        BeanUtils.copyProperties(calculatorOperation, operations);
        calculatorRepository.save(operations);
        logger.info("persisted to db store");
    }
}
