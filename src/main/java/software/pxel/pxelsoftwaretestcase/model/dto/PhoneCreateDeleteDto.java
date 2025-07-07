package software.pxel.pxelsoftwaretestcase.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record PhoneCreateDeleteDto(@Pattern(regexp = "\\d{11,13}")
                                   @NotNull
                                   String phone){
}
