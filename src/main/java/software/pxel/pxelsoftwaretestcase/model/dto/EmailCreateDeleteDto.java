package software.pxel.pxelsoftwaretestcase.model.dto;

import jakarta.validation.constraints.Email;

public record EmailCreateDeleteDto (@Email String email){
}
