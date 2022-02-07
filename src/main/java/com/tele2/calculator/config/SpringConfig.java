package com.tele2.calculator.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tele2.calculator.repository.CalculatorRepository;
import com.tele2.calculator.service.CalculatorDBStoreService;
import com.tele2.calculator.service.CalculatorFileStoreService;
import com.tele2.calculator.service.CalculatorService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
public class SpringConfig {

    @EnableJpaRepositories(basePackages = "com.tele2.calculator.repository")
    @ConditionalOnProperty(prefix = "calculator.data", name = "store", havingValue = "db")
    public static class PersistenceConfig {

        @Bean
        @ConditionalOnProperty(prefix = "calculator.data", name = "store", havingValue = "db")
        public CalculatorService calculatorDBStoreService(CalculatorRepository calculatorRepository) {
            return new CalculatorDBStoreService(calculatorRepository);
        }
    }

    @Bean
    @ConditionalOnProperty(prefix = "calculator.data", name = "store", havingValue = "file", matchIfMissing = true)
    public CalculatorService calculatorFileStoreService(ObjectMapper objectMapper) {
        return new CalculatorFileStoreService(objectMapper);
    }


}
