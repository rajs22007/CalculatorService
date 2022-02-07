package com.tele2.calculator.controller;

import com.tele2.calculator.domain.CalculatorOperation;
import com.tele2.calculator.domain.CalculatorOperationHistoryResponse;
import com.tele2.calculator.domain.CalculatorOperationResponse;
import com.tele2.calculator.enums.Operations;
import com.tele2.calculator.service.CalculatorCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/calculate")
public class CalculatorController {

    private final static String DIVIDE_BY_ZERO_ERR = "Can't divide by 0";
    private final static String HISTORY_NOT_AVAILABLE = "Calculation history not available!";

    private final CalculatorCacheService calculatorCacheService;

    @Autowired
    public CalculatorController(CalculatorCacheService calculatorCacheService) {
        this.calculatorCacheService = calculatorCacheService;
    }

    @GetMapping("/add")
    public ResponseEntity<CalculatorOperationResponse> add(@RequestParam(required = false) String id,
                                                           @RequestParam BigDecimal augend,
                                                           @RequestParam BigDecimal addend) {
        CalculatorOperation operation = calculatorCacheService.add(id, augend, addend);
        return ResponseEntity.ok(CalculatorOperationResponse.builder().operation(operation).build());
    }

    @GetMapping("/subtract")
    public ResponseEntity<CalculatorOperationResponse> subtract(@RequestParam(required = false) String id,
                                                                @RequestParam BigDecimal minuent,
                                                                @RequestParam BigDecimal subtrahend) {
        CalculatorOperation operation = calculatorCacheService.subtract(id, minuent, subtrahend);
        return ResponseEntity.ok(buildSuccessResponse(operation));
    }

    @GetMapping("/multiply")
    public ResponseEntity<CalculatorOperationResponse> multiply(@RequestParam(required = false) String id,
                                                                @RequestParam BigDecimal multiplier,
                                                                @RequestParam BigDecimal multiplicand) {
        CalculatorOperation operation = calculatorCacheService.multiply(id, multiplier, multiplicand);
        return ResponseEntity.ok(buildSuccessResponse(operation));
    }

    @GetMapping("/divide")
    public ResponseEntity<CalculatorOperationResponse> divide(@RequestParam(required = false) String id,
                                                              @RequestParam BigDecimal dividend,
                                                              @RequestParam BigDecimal divisor) {
        CalculatorOperation operation;
        if (divisor.compareTo(BigDecimal.ZERO) == 0) {
            operation = CalculatorOperation.builder().firstOperand(dividend).secondOperand(divisor)
                    .operations(Operations.DIVIDE).build();
            return ResponseEntity.badRequest().body(buildFailureResponse(operation, DIVIDE_BY_ZERO_ERR));
        }
        operation = calculatorCacheService.divide(id, dividend, divisor);
        return ResponseEntity.ok(buildSuccessResponse(operation));
    }

    @GetMapping("/history")
    public ResponseEntity<CalculatorOperationHistoryResponse> history(@RequestParam(required = false) String id) {
        List<CalculatorOperation> operations = calculatorCacheService.history(id);
        CalculatorOperationHistoryResponse response;
        String message = null;
        if (CollectionUtils.isEmpty(operations)) {
            message = HISTORY_NOT_AVAILABLE;
        }

        response = CalculatorOperationHistoryResponse.builder().operations(operations).message(message).build();
        return ResponseEntity.ok(response);
    }

    private CalculatorOperationResponse buildSuccessResponse(CalculatorOperation operation) {
        return CalculatorOperationResponse.builder().operation(operation).build();
    }

    private CalculatorOperationResponse buildFailureResponse(CalculatorOperation operation, String error) {
        return CalculatorOperationResponse.builder().operation(operation).error(error).build();
    }
}
