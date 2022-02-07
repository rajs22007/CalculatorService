package com.tele2.calculator.repository;

import com.tele2.calculator.domain.CalculatorOperations;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalculatorRepository extends JpaRepository<CalculatorOperations, String> {
}
