package tko.controller.user;

import tko.model.dto.user.AuthRequest;
import tko.model.dto.user.RegisterUsersDTO;
import tko.model.dto.user.UsersDTO;
import tko.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UsersDTO> registerUser(@RequestBody RegisterUsersDTO RegisterUsersDTO){
        UsersDTO newUser = authService.registerUser(RegisterUsersDTO);
        return ResponseEntity.ok(newUser);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest authRequest){
        String token = authService.login(authRequest.getLogin(),authRequest.getPassword());
        return ResponseEntity.ok(token);
    }
}
