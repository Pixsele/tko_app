package tko.legacy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NutritionProgramDTO {
    private Long id;
    private String description;
    private String name;
    private String type;
    private String createdBy;
    private LocalDateTime createdAt;
    private Integer likeCount;
}
