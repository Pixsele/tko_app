package tko.database.entity.workout;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "program_planned")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgramPlannedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "training_program_id", nullable = false)
    private CurrentTrainingProgramEntity currentTrainingProgram;

    @ManyToOne
    @JoinColumn(name = "planned_workout_id", nullable = false)
    private PlannedWorkoutEntity plannedWorkout;
}
