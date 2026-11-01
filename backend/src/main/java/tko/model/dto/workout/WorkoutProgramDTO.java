package tko.model.dto.workout;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutProgramDTO {
    private Long id;
    @NotNull
    private Long programId;
    @NotBlank
    private Long workoutId;
    private Integer workoutOrder;
}
