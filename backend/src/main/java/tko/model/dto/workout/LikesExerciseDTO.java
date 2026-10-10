package tko.model.dto.workout;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikesExerciseDTO {
    private Long id;
    private Long userId;
    private Long exerciseId;
    private LocalDateTime createdAt;
}
