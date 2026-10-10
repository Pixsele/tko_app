package tko.model.dto.nutrition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    private String name;
    private int kcal;
    private String unit;
    private String createdBy;
    private LocalDateTime createdAt;
}
