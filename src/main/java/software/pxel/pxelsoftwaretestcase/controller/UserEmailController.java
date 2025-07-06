package software.pxel.pxelsoftwaretestcase.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import software.pxel.pxelsoftwaretestcase.model.dto.EmailCreateDeleteDto;
import software.pxel.pxelsoftwaretestcase.model.dto.EmailUpdateDto;
import software.pxel.pxelsoftwaretestcase.security.AppUserDetails;
import software.pxel.pxelsoftwaretestcase.service.UserEmailService;

@RestController
@RequestMapping("/api/users/emails")
public class UserEmailController {

    private final UserEmailService userEmailService;

    @Autowired
    public UserEmailController(UserEmailService userEmailService) {
        this.userEmailService = userEmailService;
    }

    @PostMapping
    public ResponseEntity<?> addEmail(@AuthenticationPrincipal AppUserDetails userDetails,
                                      @RequestBody EmailCreateDeleteDto emailCreateDeleteDto){
        userEmailService.addEmail(userDetails.getUser().getId(), emailCreateDeleteDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity<?> updateEmail(@AuthenticationPrincipal AppUserDetails userDetails,
                                         @RequestBody EmailUpdateDto emailUpdateDto){
        userEmailService.updateEmail(userDetails.getUser().getId(), emailUpdateDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteEmail(@AuthenticationPrincipal AppUserDetails userDetails,
                                         @RequestBody EmailCreateDeleteDto emailCreateDeleteDto){
        userEmailService.deleteEmail(userDetails.getUser().getId(), emailCreateDeleteDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
