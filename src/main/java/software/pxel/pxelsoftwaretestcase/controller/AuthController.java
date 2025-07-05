package software.pxel.pxelsoftwaretestcase.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import software.pxel.pxelsoftwaretestcase.model.auth.AuthRequest;
import software.pxel.pxelsoftwaretestcase.model.auth.AuthResponse;
import software.pxel.pxelsoftwaretestcase.security.AppUserDetails;
import software.pxel.pxelsoftwaretestcase.security.JwtUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request){
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();

            String token = jwtUtil.generateToken(userDetails.getUser().getId());

            return new AuthResponse(token);
        }catch (AuthenticationException e){
            throw new RuntimeException("Invalid login or password");
        }
    }
}
