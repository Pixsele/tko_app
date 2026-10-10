package tko.database.entity.workout;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tko.utils.DifficultyLevel;

import java.time.LocalDateTime;

@Entity
@Table(name = "exercise")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String instruction;
    private String name;
    private String type;

    @Enumerated(EnumType.STRING)
    private DifficultyLevel difficult;

    private String specialEquipment;
    private String muscularGroup;
    private int kcal;

    @Column(name = "created_at")
    private LocalDateTime createdAt;


}