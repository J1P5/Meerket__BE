package org.j1p5.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
public class MeerketApplication {

    public static void main(String[] args) {
        SpringApplication.run(MeerketApplication.class, args);
    }
}
