package software.pxel.pxelsoftwaretestcase.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import software.pxel.pxelsoftwaretestcase.model.dto.PhoneCreateDeleteDto;
import software.pxel.pxelsoftwaretestcase.model.dto.PhoneUpdateDto;
import software.pxel.pxelsoftwaretestcase.security.AppUserDetails;
import software.pxel.pxelsoftwaretestcase.service.UserPhoneService;

@RestController
@RequestMapping("/api/users/phones")
public class UserPhoneController {

    private final UserPhoneService userPhoneService;

    @Autowired
    public UserPhoneController(UserPhoneService userPhoneService) {
        this.userPhoneService = userPhoneService;
    }

    @PostMapping
    public ResponseEntity<?> addPhone(@AuthenticationPrincipal AppUserDetails userDetails,
                                      @Valid @RequestBody PhoneCreateDeleteDto phoneCreateDeleteDto){
        try {
            userPhoneService.addPhone(userDetails.getUser().getId(), phoneCreateDeleteDto);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<?> updatePhone(@AuthenticationPrincipal AppUserDetails userDetails,
                                         @Valid @RequestBody PhoneUpdateDto phoneUpdateDto){
        try {
            userPhoneService.updatePhone(userDetails.getUser().getId(), phoneUpdateDto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deletePhone(@AuthenticationPrincipal AppUserDetails userDetails,
                                         @Valid @RequestBody PhoneCreateDeleteDto phoneCreateDeleteDto){
        try {
            userPhoneService.deletePhone(userDetails.getUser().getId(), phoneCreateDeleteDto);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
