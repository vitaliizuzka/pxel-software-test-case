package software.pxel.pxelsoftwaretestcase.model.dto;

import java.time.LocalDate;
import java.util.List;

public record UserReadDto (Long id,
                           String name,
                           LocalDate dateOfBirth,
                           List<String> emails,
                           List<String> phones) {
}
