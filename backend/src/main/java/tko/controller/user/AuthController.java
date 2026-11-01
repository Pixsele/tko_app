package tko.controller.user;

import tko.database.entity.user.UsersEntity;
import tko.database.repository.user.UsersRepository;
import tko.model.dto.user.AuthRequest;
import tko.model.dto.user.RegisterUsersDTO;
import tko.model.dto.user.UsersDTO;
import tko.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class AuthController {
    private final AuthService authService;
    private final UsersRepository usersRepository;

    public AuthController(AuthService authService, UsersRepository usersRepository) {
        this.authService = authService;
        this.usersRepository = usersRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<UsersDTO> registerUser(@RequestBody RegisterUsersDTO RegisterUsersDTO){
        UsersDTO newUser = authService.registerUser(RegisterUsersDTO);
        return ResponseEntity.ok(newUser);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>> login(@RequestBody AuthRequest authRequest){
        String token = authService.login(authRequest.getLogin(),authRequest.getPassword());

        UsersEntity user = usersRepository.findByLogin(authRequest.getLogin());

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("token", token);
        response.put("userId", user.getId());
        response.put("name", user.getName());

        return ResponseEntity.ok(response);
    }
}
