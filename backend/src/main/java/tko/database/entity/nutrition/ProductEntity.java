package tko.database.entity.nutrition;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int kcal;
    private String unit;
    private BigDecimal grams;
    private BigDecimal portion;

    private BigDecimal fats;
    private BigDecimal carbs;
    private BigDecimal proteins;


    @Column(name = "created_at")
    private LocalDateTime createdAt;
}