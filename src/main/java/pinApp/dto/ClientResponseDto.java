package pinApp.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ClientResponseDto {
    private String firstName;
    private String lastName;
    private Long dni;
    private Integer age;
    private LocalDate birthDate;
    private LocalDate estimatedDateOfDeath;
}
