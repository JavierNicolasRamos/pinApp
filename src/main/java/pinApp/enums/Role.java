package pinApp.enums;

import lombok.Getter;

@Getter
public enum Role {
    ADMIN("Admin");

    private final String description;
    Role(String description) {
        this.description = description;
    }
}
