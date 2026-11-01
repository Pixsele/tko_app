package tko.model.dto.nutrition;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    @NotBlank
    private String name;
    @NotNull
    private Integer kcal;
    @NotBlank
    private String unit;
    @NotNull
    private BigDecimal grams;
    @NotNull
    private BigDecimal portion;

    private LocalDateTime createdAt;

    @NotNull
    private BigDecimal fats;
    @NotNull
    private BigDecimal carbs;
    @NotNull
    private BigDecimal proteins;
}
