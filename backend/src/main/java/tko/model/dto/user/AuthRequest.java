package tko.model.dto.user;

import lombok.Data;

@Data
public class AuthRequest {
    private String login;
    private String password;
}
