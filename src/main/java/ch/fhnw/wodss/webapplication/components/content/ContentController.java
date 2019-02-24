package ch.fhnw.wodss.webapplication.components.content;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@Api(tags = "Static sites", description = "Endpoint for serving all static content (No-rest!)")
public class ContentController {

    @GetMapping("/")
    @ApiOperation("Show the static index page")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Static rendered index page")
    })
    public String showIndex() {
        return "Hello from the index page!";
    }

    @GetMapping("/user")
    @ApiOperation("Show the static user page")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Static rendered user page")
    })
    public String showUser() {
        return "Hello from the user page!";
    }

    @GetMapping("/admin")
    @ApiOperation("Show the static admin page")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Static rendered admin page")
    })
    public String showAdmin() {
        return "Hello from the admin page!";
    }
}
