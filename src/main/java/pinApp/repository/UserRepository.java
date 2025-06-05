package pinApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pinApp.entity.User;
import pinApp.enums.Role;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(@Param("email") String email);

    @Query(value = "SELECT * FROM users WHERE email = :email AND is_active = TRUE", nativeQuery = true)
    User findByCustomerEmail(@Param("email")String email);

    @Query(value = "SELECT email FROM users WHERE email = :email AND is_active = TRUE", nativeQuery = true)
    String getNameByEmail(String email);

    @Query(value = "SELECT user_role FROM users WHERE email = :email AND is_active = TRUE", nativeQuery = true)
    Role getRoleByEmail(String email);
}
