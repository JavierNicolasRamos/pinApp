package pinApp.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import pinApp.entity.Client;
import pinApp.entity.User;
import pinApp.enums.Role;
import pinApp.repository.ClientRepository;
import pinApp.repository.UserRepository;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.*;

@Configuration
public class DataSeedConfig {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    @Order(1)
    public List<Map<String, Object>> usersDataSeed() throws IOException {
        InputStream usersInputStream = new ClassPathResource("usersDataSeed.json").getInputStream();
        ObjectMapper usersObjectMapper = new ObjectMapper();

        return usersObjectMapper.readValue(usersInputStream, new TypeReference<>() {
        });
    }

    @Bean
    @Order(2)
    public List<User> createUsers(@Qualifier("usersDataSeed") @NotNull List<Map<String, Object>> usersDataSeed) {
        List<User> users = new ArrayList<>();

        for (Map<String, Object> userData : usersDataSeed) {
            String email = (String) userData.get("email");
            String password = (String) userData.get("password");

            String roleString = (String) userData.get("customerRole");
            Role role = Role.valueOf(roleString.toUpperCase());

            Optional<User> userExist = this.userRepository.findByEmail(email);
            if (userExist.isEmpty()) {
                User user = new User();
                user.setEmail(email);
                String encryptedPassword = this.passwordEncoder.encode(password);
                user.setPassword(encryptedPassword);
                user.setUserRole(role);
                user.setIsActive(true);
                this.userRepository.save(user);

                users.add(user);
            }
        }

        return users;
    }

    @Bean
    @Order(3)
    public List<Map<String, Object>> clientsDataSeed() throws IOException {
        InputStream clientsInputStream = new ClassPathResource("clientsDataSeed.json").getInputStream();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Soporta LocalDate
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        return objectMapper.readValue(clientsInputStream, new TypeReference<>() {});
    }

    @Bean
    @Order(4)
    public List<Client> createClients(@Qualifier("clientsDataSeed") @NotNull List<Map<String, Object>> clientsDataSeed) {
        List<Client> clients = new ArrayList<>();

        for (Map<String, Object> clientData : clientsDataSeed) {
            Long dni = Long.valueOf(clientData.get("dni").toString());

            Optional<Client> existingClient = clientRepository.findByDni(dni);
            if (existingClient.isEmpty()) {
                Client client = new Client();
                client.setFirstName((String) clientData.get("firstName"));
                client.setLastName((String) clientData.get("lastName"));
                client.setDni(dni);
                client.setAge((Integer) clientData.get("age"));
                client.setBirthDate(LocalDate.parse((String) clientData.get("birthDate")));

                clientRepository.save(client);
                clients.add(client);
            }
        }

        return clients;
    }
}
