package ch.fhnw.wodss.webapplication.components.employee;

public enum Role {
    ADMINISTRATOR("ADMINISTRATOR"),
    PROJECTMANAGER("PROJECTMANAGER"),
    DEVELOPER("DEVELOPER");

    private final String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}
