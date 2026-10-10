package tko.model.dto.workout;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseDTO {
    private Long id;
    private String instruction;
    private String name;
    private String type;
    private String difficult;
    private String specialEquipment;
    private String muscularGroup;
    private Integer kcal;
    private LocalDateTime createdAt;
}