package tko.model.dto.workout;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgramPlannedDTO {
    private Long id;
    @NotNull
    private Long currentTrainingProgramId;
    @NotNull
    private Long plannedWorkoutId;
}