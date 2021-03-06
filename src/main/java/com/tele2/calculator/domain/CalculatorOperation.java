package com.tele2.calculator.domain;

import com.tele2.calculator.enums.Operations;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalculatorOperation implements Serializable {
    private String id;
    private BigDecimal firstOperand;
    private BigDecimal secondOperand;
    private Operations operations;
    private BigDecimal result;
}
