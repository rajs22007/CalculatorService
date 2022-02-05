package com.tele2.calculator.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class CalculatorOperationHistoryResponse {
    private List<CalculatorOperation> operations;
    private String message;
}
