package com.tele2.calculator.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CalculatorOperationResponse {
    private CalculatorOperation operation;
    private String error;

}
