package tko.model.dto.user;

import tko.utils.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsersDTO {
    private Long id;
    private String name;
    private String login;
    private Integer age;
    private Double weight;
    private Double height;
    private Integer targetKcal;
    private Gender gender;

    private Long currentTrainingProgramId;
    private Long currentNutritionProgramId;
    private LocalDateTime createdAt;
}
