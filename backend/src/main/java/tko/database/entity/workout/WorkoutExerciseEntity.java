package tko.database.entity.workout;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "workout_exercises", uniqueConstraints = @UniqueConstraint(columnNames = {"workout_id", "exercise_order"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutExerciseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "workout_id")
    private WorkoutEntity workout;

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private ExerciseEntity exercise;

    @Column(name = "exercise_order")
    private Integer exerciseOrder;

    private Integer sets;
    private Integer reps;
    private Double distance;
    private Double duration;
    private Integer restTime;
}