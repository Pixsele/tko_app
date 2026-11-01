package tko.model.dto.nutrition;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tko.utils.MealType;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KcalProductDTO {
    private Long id;
    @NotNull
    private Long kcalTrackerId;
    @NotNull
    private Long productId;
    @NotNull
    private Integer count;
    @NotBlank
    private String typeMeal;
}
