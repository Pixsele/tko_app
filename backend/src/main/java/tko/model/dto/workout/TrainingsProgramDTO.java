package tko.model.dto.workout;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingsProgramDTO {
    private Long id;
    private String description;
    private String name;
    private String type;
    private String difficult;
    private String createdBy;
    private LocalDateTime createdAt;
    private List<WorkoutProgramDTO> workoutPrograms;
}