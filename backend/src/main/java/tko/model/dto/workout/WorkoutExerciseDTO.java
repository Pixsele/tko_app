package tko.model.dto.workout;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutExerciseDTO {
    private Long id;
    private Long workoutId;
    private Long exerciseId;
    private int sets;
    private int reps;
    private double distance;
    private double duration;
    private int restTime;
}