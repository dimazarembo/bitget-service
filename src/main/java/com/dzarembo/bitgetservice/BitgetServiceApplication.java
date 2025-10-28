package com.dzarembo.bitgetservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BitgetServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BitgetServiceApplication.class, args);
    }

}
