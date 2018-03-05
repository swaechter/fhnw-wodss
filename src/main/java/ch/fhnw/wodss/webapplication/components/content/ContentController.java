package ch.fhnw.wodss.webapplication.components.content;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class ContentController {

    @GetMapping("/")
    public String showIndex() {
        return "Hello from the index page!";
    }

    @GetMapping("/user/index")
    public String showUser() {
        return "Hello from the user page!";
    }

    @GetMapping("/admin/index")
    public String showAdmin() {
        return "Hello from the admin page!";
    }
}
