package tko.controller.user;

import tko.model.dto.user.UsersDTO;
import tko.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UsersController {

    private final UsersService usersService;

    @Autowired
    UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsersDTO> getUser(@PathVariable Long id){
        UsersDTO usersDTO = usersService.getUser(id);
        return ResponseEntity.ok(usersDTO);
    }
}
