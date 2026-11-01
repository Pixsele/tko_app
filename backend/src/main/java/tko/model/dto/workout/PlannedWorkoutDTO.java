package tko.model.dto.workout;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlannedWorkoutDTO {
    private Long id;
    @NotNull
    private Long userId;
    @NotNull
    private Long workoutId;
    @NotNull
    private LocalDate date;
    private String status;
}
