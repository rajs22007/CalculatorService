package com.tele2.calculator.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.tele2.calculator.domain.CalculatorOperation;
import com.tele2.calculator.domain.CalculatorOperations;
import com.tele2.calculator.enums.Operations;
import com.tele2.calculator.repository.CalculatorRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class CalculatorServiceTest extends TestUtility{

    @InjectMocks
    private CalculatorDBStoreService calculatorDBStoreService;
    @InjectMocks
    private CalculatorFileStoreService calculatorFileStoreService;
    @Mock
    private CalculatorRepository calculatorRepository;
    @Mock
    private Resource resultFileResource;
    @Mock
    private File testFile;
    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    void init() {
        ReflectionTestUtils.setField(calculatorFileStoreService, "resultFileResource", resultFileResource);
    }

    @Test
    void testLoadOperationHistoryData() {
        CalculatorOperations operations = calculatorOperations();
        when(calculatorRepository.findAll()).thenReturn(Collections.singletonList(operations));
        List<CalculatorOperation> operationList = calculatorDBStoreService.loadOperationHistoryData();
        Assertions.assertNotNull(operationList);
        Assertions.assertEquals(1, operationList.size());

        operationList = calculatorFileStoreService.loadOperationHistoryData();
        Assertions.assertNotNull(operationList);
        Assertions.assertEquals(0, operationList.size());
    }

    @Test
    void testPersistOperation() {
        CalculatorOperation operation = calculatorOperation();

        CalculatorOperations operations = calculatorOperations();
        when(calculatorRepository.save(Mockito.any(CalculatorOperations.class))).thenReturn(operations);
        calculatorDBStoreService.persistOperation(operation);

        try {
            when(resultFileResource.getFile()).thenReturn(testFile);

            when(objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true)).thenReturn(objectMapper);
            Mockito.doNothing().when(objectMapper).writeValue(any(File.class), any());
            calculatorFileStoreService.persistOperation(operation);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testLoadOperationHistory() {
        CalculatorOperation operation = calculatorOperation();
        try {
            String resourceName = "data/calculation-results.json";

            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource(resourceName).getFile());
            when(resultFileResource.getFile()).thenReturn(file);
            when(objectMapper.enable(any(DeserializationFeature.class))).thenReturn(objectMapper);
            when(objectMapper.readValue(any(Reader.class), any(TypeReference.class))).thenReturn(Collections.singletonList(operation));
            calculatorFileStoreService.loadOperationHistory();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
