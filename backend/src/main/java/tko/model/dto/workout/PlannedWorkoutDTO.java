package tko.model.dto.workout;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlannedWorkoutDTO {
    private Long id;
    private Long userId;
    private Long workoutId;
    private LocalDate date;
    private String status;
}
