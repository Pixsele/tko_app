package tko.model.dto.workout;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutProgramDTO {
    private Long id;
    private Long programId;
    private Long workoutId;
    private LocalDate date;
}
