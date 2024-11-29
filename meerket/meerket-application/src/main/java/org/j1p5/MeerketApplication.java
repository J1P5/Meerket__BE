package org.j1p5;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
//@ComponentScan(basePackages = "org.j1p5")
//@EntityScan(basePackages = "org.j1p5") // 엔티티 스캔
public class MeerketApplication {

    public static void main(String[] args) {
        SpringApplication.run(MeerketApplication.class, args);
    }
}
