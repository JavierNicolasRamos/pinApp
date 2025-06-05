package pinApp.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pinApp.dto.ClientMetricsDto;
import pinApp.dto.ClientRequestDto;
import pinApp.dto.ClientResponseDto;
import pinApp.entity.Client;
import pinApp.repository.ClientRepository;
import pinApp.service.config.AuthService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CONFLICT;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    private static final int LIFE_EXPECTANCY_YEARS = 80;

    private static final Logger logger = LogManager.getLogger(ClientService.class);

    public Client createClient(ClientRequestDto request) {
        logger.info("Attempting to create client with DNI: {}", request.getDni());

        if (clientRepository.findByDni(request.getDni()).isPresent()) {
            logger.warn("Client with DNI {} already exists. Throwing conflict exception.", request.getDni());
            throw new ResponseStatusException(CONFLICT, "Client with DNI " + request.getDni() + " already exists.");
        }

        Client client = new Client();
        client.setFirstName(request.getFirstName());
        client.setLastName(request.getLastName());
        client.setDni(request.getDni());
        client.setAge(request.getAge());
        client.setBirthDate(request.getBirthDate());

        Client savedClient = clientRepository.save(client);
        logger.info("Client created successfully with ID: {}", savedClient.getId());
        return savedClient;
    }

    public List<ClientResponseDto> getAllClientsWithEstimates() {
        logger.info("Fetching all clients with estimated date of death.");

        List<ClientResponseDto> result = clientRepository.findAll()
                .stream()
                .map(client -> {
                    ClientResponseDto dto = new ClientResponseDto();
                    dto.setFirstName(client.getFirstName());
                    dto.setLastName(client.getLastName());
                    dto.setDni(client.getDni());
                    dto.setAge(client.getAge());
                    dto.setBirthDate(client.getBirthDate());
                    dto.setEstimatedDateOfDeath(client.getBirthDate().plusYears(LIFE_EXPECTANCY_YEARS));
                    return dto;
                })
                .collect(Collectors.toList());

        logger.info("Successfully fetched {} clients with estimates.", result.size());
        return result;
    }

    public ClientMetricsDto getClientMetrics() {
        logger.info("Calculating client metrics.");

        List<Client> clients = clientRepository.findAll();
        logger.debug("Retrieved {} clients from repository.", clients.size());

        List<Integer> ages = clients.stream()
                .map(Client::getAge)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        double average = ages.stream().mapToInt(Integer::intValue).average().orElse(0.0);
        double stdDev = calculateStandardDeviation(ages, average);

        Map<String, Long> ageGroups = clients.stream()
                .filter(c -> c.getAge() != null)
                .collect(Collectors.groupingBy(
                        c -> {
                            int lower = (c.getAge() / 10) * 10;
                            int upper = lower + 9;
                            return lower + "-" + upper;
                        },
                        Collectors.counting()
                ));

        logger.info("Metrics calculated: averageAge = {}, standardDeviation = {}", average, stdDev);
        logger.debug("Age group distribution: {}", ageGroups);

        ClientMetricsDto metrics = new ClientMetricsDto();
        metrics.setAverageAge(average);
        metrics.setStandardDeviation(stdDev);
        metrics.setAgeGroups(new TreeMap<>(ageGroups));

        return metrics;
    }

    private double calculateStandardDeviation(List<Integer> values, double mean) {
        if (values.isEmpty()) {
            logger.warn("Standard deviation calculation received an empty list.");
            return 0.0;
        }
        double variance = values.stream()
                .mapToDouble(age -> Math.pow(age - mean, 2))
                .sum() / values.size();
        double stdDev = Math.sqrt(variance);
        logger.debug("Standard deviation calculated: {}", stdDev);
        return stdDev;
    }
}
