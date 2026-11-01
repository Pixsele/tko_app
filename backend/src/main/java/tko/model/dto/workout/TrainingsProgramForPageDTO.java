package tko.model.dto.workout;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingsProgramForPageDTO {
    private Long id;
    private String name;
    private Integer likeCount;
    private boolean liked;
}
