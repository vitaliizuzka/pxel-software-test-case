package software.pxel.pxelsoftwaretestcase.model.dto;

import jakarta.validation.constraints.Email;

public record EmailUpdateDto(@Email String oldEmail,
                             @Email String newEmail) {
}
