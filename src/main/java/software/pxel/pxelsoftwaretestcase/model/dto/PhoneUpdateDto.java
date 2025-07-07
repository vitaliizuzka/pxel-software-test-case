package software.pxel.pxelsoftwaretestcase.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record PhoneUpdateDto (@Pattern(regexp = "\\d{11,13}")
                              @NotNull
                              String oldPhone,
                              @Pattern(regexp = "\\d{11,13}")
                              @NotNull
                              String newPhone){
}
