package com.tele2.calculator.enums;

import java.math.BigDecimal;
import java.math.RoundingMode;

public enum Operations {
    ADD("+") {
        public BigDecimal apply(BigDecimal augend, BigDecimal addend) {

            return augend.add(addend);
        }
    },

    SUBTRACT("-") {
        public BigDecimal apply(BigDecimal minuent, BigDecimal subtrahend) {
            return minuent.subtract(subtrahend);
        }
    },

    MULTIPLY("*") {
        public BigDecimal apply(BigDecimal multiplier, BigDecimal multiplicand) {
            return multiplier.multiply(multiplicand);
        }
    },

    DIVIDE("/") {
        public BigDecimal apply(BigDecimal dividend, BigDecimal divisor) {
            return dividend.divide(divisor, RoundingMode.HALF_EVEN);
        }
    };

    private final String action;

    Operations(String action) {
        this.action = action;
    }

    public abstract BigDecimal apply(BigDecimal firstOperand, BigDecimal secondOperand);

    public String getAction() {
        return action;
    }
}
