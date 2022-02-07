package com.tele2.calculator.domain;

import com.tele2.calculator.enums.Operations;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Getter
@Setter
@Entity(name = "Calculator_Operations")
public class CalculatorOperations {
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "first_operand")
    private BigDecimal firstOperand;
    @Column(name = "second_operand")
    private BigDecimal secondOperand;
    @Column(name = "operations")
    private Operations operations;
    @Column(name = "result")
    private BigDecimal result;
}
