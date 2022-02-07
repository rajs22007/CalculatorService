package com.tele2.calculator.service;

import com.tele2.calculator.domain.CalculatorOperation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@ExtendWith(SpringExtension.class)
public class CalculatorCacheServiceTest extends TestUtility {
    @InjectMocks
    private CalculatorDefaultCacheService calculatorDefaultCacheService;
    @Mock
    private CalculatorService calculatorService;

    @BeforeEach
    void init() {
        Mockito.when(calculatorService.loadOperationHistoryData()).thenReturn(Collections.singletonList(calculatorOperation()));
        calculatorDefaultCacheService.loadOperationHistoryMap();
    }

    @Test
    void testAdd() {
        CalculatorOperation addOperation = calculatorDefaultCacheService.add(null, BigDecimal.ONE, BigDecimal.TEN);
        Assertions.assertNotNull(addOperation);
    }

    @Test
    void testSubtract() {
        CalculatorOperation subtractOperation = calculatorDefaultCacheService.subtract(null, BigDecimal.ONE, BigDecimal.TEN);
        Assertions.assertNotNull(subtractOperation);
    }

    @Test
    void testMultiply() {
        CalculatorOperation multiplyOperation = calculatorDefaultCacheService.multiply(null, BigDecimal.ONE, BigDecimal.TEN);
        Assertions.assertNotNull(multiplyOperation);
    }

    @Test
    void testDivide() {
        CalculatorOperation divideOperation = calculatorDefaultCacheService.divide(null, BigDecimal.ONE, BigDecimal.TEN);
        Assertions.assertNotNull(divideOperation);
    }

    @Test
    void testHistory() {
        List<CalculatorOperation> operations = calculatorDefaultCacheService.history(null);
        Assertions.assertNotNull(operations);
        Assertions.assertFalse(operations.isEmpty());

        operations = calculatorDefaultCacheService.history("TEST_ID");
        Assertions.assertNotNull(operations);
    }
}
