package tko.database.entity.workout;

import tko.database.entity.user.UsersEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "planned_workout")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlannedWorkoutEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UsersEntity user;

    @ManyToOne
    @JoinColumn(name = "workout_id")
    private WorkoutEntity workout;

    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private WorkoutStatus status;

    public enum WorkoutStatus {
        PLANNED, COMPLETED
    }
}