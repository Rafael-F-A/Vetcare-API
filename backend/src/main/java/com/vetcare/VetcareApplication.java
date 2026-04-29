package com.vetcare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VetcareApplication {
    public static void main(String[] args) {
        SpringApplication.run(VetcareApplication.class, args);
        System.out.println("=== VETCARE API INICIADA COM SUCESSO! ===");
        System.out.println("Acesse: http://localhost:8080/api/tutores");
    }
}