package tko.model.dto.workout;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutExerciseDTO {
    private Long id;
    @NotNull
    private Long workoutId;
    @NotNull
    private Long exerciseId;
    @NotNull
    private Integer sets;
    @NotNull
    private Integer reps;
    @NotNull
    private Double distance;
    @NotNull
    private Double duration;
    @NotNull
    private Integer restTime;
    private Integer exerciseOrder;
}