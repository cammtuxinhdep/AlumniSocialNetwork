/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vmct.configs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 *
 * @author HP
 */
@SpringBootApplication
@EnableScheduling
public class ApplicationConfigs {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationConfigs.class, args);
    }
}
