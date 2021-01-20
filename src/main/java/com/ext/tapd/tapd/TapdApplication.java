package com.ext.tapd.tapd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * @author lx
 */
@SpringBootApplication
@EnableScheduling
public class TapdApplication {

    public static void main(String[] args) {
        SpringApplication.run(TapdApplication.class, args);
    }

}
