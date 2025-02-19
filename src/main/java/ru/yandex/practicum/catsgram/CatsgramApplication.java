package ru.yandex.practicum.catsgram;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CatsgramApplication {
    //    private static final Logger log = LoggerFactory.getLogger(CatsgramApplication.class);
    public static void main(final String[] args) {
        SpringApplication.run(CatsgramApplication.class, args);
    }
}
