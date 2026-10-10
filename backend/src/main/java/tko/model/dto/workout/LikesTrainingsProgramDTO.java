package tko.model.dto.workout;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikesTrainingsProgramDTO {
    private Long id;
    private Long userId;
    private Long trainingsProgramId;
    private LocalDateTime createdAt;
}
