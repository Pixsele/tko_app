package tko.model.dto.nutrition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductForPageDTO {
    private Long id;
    private String name;
    private Integer calories;
    private Integer percentOfTarget;
    private String unit;
    private BigDecimal portion;
}
