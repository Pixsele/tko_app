package tko.model.dto.workout;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikesWorkoutDTO {
    private Long id;
    @NotNull
    private Long userId;
    @NotNull
    private Long workoutId;
}