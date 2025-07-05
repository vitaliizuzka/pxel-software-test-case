package software.pxel.pxelsoftwaretestcase.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransferRequestDto (
        @NotNull
        Long userIdTo,
        @NotNull
        @DecimalMin(value = "0.01")
        BigDecimal amount){
}
