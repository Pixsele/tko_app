package tko.legacy;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tko.database.entity.nutrition.ProductEntity;

import java.time.LocalDate;

@Entity
@Table(name = "nutrition_plan_product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NutritionPlanProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "program_id")
    private NutritionProgramEntity nutritionProgram;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    private Integer count;
    private String typeMeal;
    private LocalDate date;
}