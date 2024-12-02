package org.j1p5;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// @ComponentScan(basePackages = "org.j1p5")
// @EntityScan(basePackages = "org.j1p5") // 엔티티 스캔
public class MeerketApplication {

    public static void main(String[] args) {
        SpringApplication.run(MeerketApplication.class, args);
    }
}
