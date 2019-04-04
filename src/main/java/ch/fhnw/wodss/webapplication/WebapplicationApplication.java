package ch.fhnw.wodss.webapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@SpringBootApplication
public class WebapplicationApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebapplicationApplication.class, args);
    }

    @Controller
    @RequestMapping("/")
    public class IndexController {

        @GetMapping
        public String redirectToSwagger() {
            return "redirect:/swagger-ui.html";
        }

        @RequestMapping("/env")
        public @ResponseBody
        Map<String, String> getEnvironment() {
            return System.getenv();
        }
    }
}
