package software.pxel.pxelsoftwaretestcase.controller;

import io.jsonwebtoken.Jwt;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import software.pxel.pxelsoftwaretestcase.model.dto.TransferRequestDto;
import software.pxel.pxelsoftwaretestcase.repository.AccountRepository;
import software.pxel.pxelsoftwaretestcase.security.AppUserDetails;
import software.pxel.pxelsoftwaretestcase.service.AccountService;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@AuthenticationPrincipal AppUserDetails userDetails,
                                      @Valid @RequestBody TransferRequestDto transferRequest) {
        Long userIdFrom = userDetails.getUser().getId();
        try {
            accountService.transfer(userIdFrom, transferRequest.userIdTo(), transferRequest.amount());
            return ResponseEntity.ok("Transfer successful");
        } catch (IllegalArgumentException | IllegalStateException | EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("internal server error");
        }

    }
}
