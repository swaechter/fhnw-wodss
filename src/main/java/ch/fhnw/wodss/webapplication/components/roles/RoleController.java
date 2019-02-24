package ch.fhnw.wodss.webapplication.components.roles;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/role")
@Api(tags = "Roles", description = "Endpoint for retrieving roles")
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping()
    @ApiOperation("Get all roles")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "All account roles")
    })
    public List<Role> getRoles() {
        return roleService.getRoles();
    }
}
