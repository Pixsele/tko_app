package tko.service;

import tko.database.entity.user.UsersEntity;
import tko.database.repository.user.UsersRepository;
import tko.model.dto.user.RegisterUsersDTO;
import tko.model.dto.user.UsersDTO;
import tko.exception.UserAlreadyExists;
import tko.model.mapper.UsersMapper;
import tko.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class AuthService {
    private final UsersRepository usersRepository;
    private final UsersMapper usersMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UsersRepository usersRepository, UsersMapper usersMapper, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.usersRepository = usersRepository;
        this.usersMapper = usersMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public UsersDTO registerUser(RegisterUsersDTO registerUsersDTO) {
        UsersEntity usersEntity = new UsersEntity();

        usersEntity.setName(registerUsersDTO.getName());
        if(usersRepository.existsByLogin(usersEntity.getLogin())) {
            throw new UserAlreadyExists(usersEntity.getLogin());
        }
        usersEntity.setLogin(registerUsersDTO.getLogin());
        usersEntity.setPassword(passwordEncoder.encode(registerUsersDTO.getPassword()));
        usersEntity.setBirthDay(registerUsersDTO.getBirthDay());
        usersEntity.setWeight(registerUsersDTO.getWeight());
        usersEntity.setHeight(registerUsersDTO.getHeight());
        usersEntity.setTargetKcal(1900);
        usersEntity.setGender(registerUsersDTO.getGender());
        usersEntity.setCurrentTrainingProgram(null);
        usersEntity.setCurrentNutritionProgram(null);
        usersEntity.setCreatedAt(LocalDateTime.now());
        usersEntity.setRole("ROLE_USER");

        UsersEntity savedUser = usersRepository.save(usersEntity);

        return usersMapper.toDTO(savedUser);
    }

    public String login(String login, String password) {
        UsersEntity usersEntity = usersRepository.findByLogin(login);
        if(usersEntity != null && passwordEncoder.matches(password, usersEntity.getPassword())) {
            return jwtUtil.generateToken(usersEntity);
        }
        throw new RuntimeException("Invalid login or password");
    }

}
