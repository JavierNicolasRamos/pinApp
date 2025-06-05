package pinApp.entity;

import jakarta.persistence.*;
import lombok.*;
import pinApp.enums.Role;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long id;

    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role userRole;
    private Boolean isActive;
}
