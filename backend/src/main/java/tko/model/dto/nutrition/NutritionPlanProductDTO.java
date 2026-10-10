package tko.model.dto.nutrition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NutritionPlanProductDTO {
    private Long id;
    private Long nutritionProgramId;
    private Long productId;
    private int count;
    private String typeMeal;
    private LocalDate date;
}