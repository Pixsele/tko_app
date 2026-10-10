package tko.database.entity.nutrition;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "nutrition_program")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NutritionProgramEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private String name;
    private String type;
    private String createdBy;
    private LocalDateTime createdAt;
}
