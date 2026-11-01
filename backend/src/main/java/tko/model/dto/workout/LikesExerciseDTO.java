package tko.model.dto.workout;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikesExerciseDTO {
    private Long id;
    @NotNull
    private Long userId;
    @NotNull
    private Long exerciseId;
}
