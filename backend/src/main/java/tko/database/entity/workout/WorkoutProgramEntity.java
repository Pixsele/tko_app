package tko.database.entity.workout;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "workout_program")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutProgramEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "program_id")
    private TrainingsProgramEntity program;

    @ManyToOne
    @JoinColumn(name = "workout_id")
    private WorkoutEntity workout;

    private Integer workoutOrder;
}
