package ch.fhnw.wodss.webapplication.components.employee;

public enum Role {
    ADMINISTRATOR,//("ADMINISTRATOR"),
    PROJECTMANAGER,//("PROJECTMANAGER"),
    DEVELOPER;//("DEVELOPER");

    /*@JsonCreator
    public static Role getRoleFromValue(String value) {
        for (Role role : values()) {
            if (role.roleName.equals(value)) {// .valueInJson.equals(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException();
    }*/

/*    private final String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }*/
}
