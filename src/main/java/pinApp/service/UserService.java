package pinApp.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pinApp.enums.Role;
import pinApp.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private static Logger logger = LogManager.getLogger(UserService.class);

    public String getNameByEmail(String email) {
        try {
            return userRepository.getNameByEmail(email);
        } catch (Exception e) {
            logger.error("Error al buscar el usuario por email: " + e.getMessage());
            throw new RuntimeException("Error inesperado al internar obtener el nombre del usuario con email:" + email);
        }
    }

    public Role getRoleByEmail(String email) throws Exception {
        try {
            return userRepository.getRoleByEmail(email);
        } catch (Exception e) {
            logger.error("Error al buscar el usuario por email: " + e.getMessage());
            throw new RuntimeException("Error al buscar el usuario por email: " + e.getMessage());
        }
    }


}
