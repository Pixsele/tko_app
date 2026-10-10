package tko.database.entity.nutrition;

import tko.database.entity.user.UsersEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "likes_nutrition_program", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "nutrition_program_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikesNutritionProgramEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UsersEntity user;

    @ManyToOne
    @JoinColumn(name = "nutrition_program_id")
    private NutritionProgramEntity nutritionProgram;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}