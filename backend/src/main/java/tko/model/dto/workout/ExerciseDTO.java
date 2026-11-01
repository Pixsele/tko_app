package tko.model.dto.workout;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseDTO {
    private Long id;
    @NotBlank
    private String instruction;
    @NotBlank
    private String name;
    @NotBlank
    private String difficult;
    @NotBlank
    private String muscularGroup;
    private Integer likeCount;
    @NotNull
    private Boolean requiresEquipment;
    @NotBlank
    private String type;
}