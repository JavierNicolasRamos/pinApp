package pinApp.dto;

import lombok.Data;
import java.util.Map;

@Data
public class ClientMetricsDto {
    private double averageAge;
    private double standardDeviation;
    private Map<String, Long> ageGroups;
}