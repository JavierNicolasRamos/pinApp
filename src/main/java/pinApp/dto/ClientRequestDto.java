package pinApp.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ClientRequestDto {
    private String firstName;
    private String lastName;
    private Integer age;
    private LocalDate birthDate;
    private Long dni;
}