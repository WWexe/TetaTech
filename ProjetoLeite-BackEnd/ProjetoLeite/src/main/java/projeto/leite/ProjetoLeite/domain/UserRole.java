package projeto.leite.ProjetoLeite.domain;

public enum UserRole {
    ADMIN("admin"),
    COLLECTOR("collector"),
    PRODUCER("producer");

    private String role;

    UserRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}


