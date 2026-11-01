package tko.model.dto.workout;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutDTO {
    private Long id;
    @NotBlank
    private String description;
    @NotBlank
    private String name;
    @NotBlank
    private String difficult;
    private Integer likeCount;
}
