package ch.fhnw.wodss.webapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebApplication {

    static {
        System.getProperties().setProperty("org.jooq.no-logo", "true");
    }

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }
}
