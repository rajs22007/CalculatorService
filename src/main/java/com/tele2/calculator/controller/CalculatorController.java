package com.tele2.calculator.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RestController
@RequestMapping("/calculate")
public class CalculatorController {

    @GetMapping("/add")
    public ResponseEntity<String> add(@RequestParam BigDecimal augend, @RequestParam BigDecimal addend) {
        return ResponseEntity.ok(augend.add(addend).toString());
    }

    @GetMapping("/subtract")
    public ResponseEntity<String> subtract(@RequestParam BigDecimal minuent, @RequestParam BigDecimal subtrahend) {
        return ResponseEntity.ok(minuent.subtract(subtrahend).toString());
    }

    @GetMapping("/multiply")
    public ResponseEntity<String> multiply(@RequestParam BigDecimal multiplier, @RequestParam BigDecimal multiplicand) {
        return ResponseEntity.ok(multiplier.multiply(multiplicand).toString());
    }

    @GetMapping("/divide")
    public ResponseEntity<String> divide(@RequestParam BigDecimal dividend, @RequestParam BigDecimal divisor) {
        if (BigDecimal.ZERO.equals(divisor)) {
            return ResponseEntity.badRequest().body("division by zero not allowed");
        }
        return ResponseEntity.ok(dividend.divide(divisor, RoundingMode.HALF_EVEN).toString());
    }
}
