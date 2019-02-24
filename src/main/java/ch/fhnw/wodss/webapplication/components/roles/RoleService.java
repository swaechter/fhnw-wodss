package ch.fhnw.wodss.webapplication.components.roles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role createRole(String roleName) {
        return roleRepository.save(new Role(roleName));
    }

    public List<Role> getRoles() {
        return roleRepository.findAll();
    }
}
