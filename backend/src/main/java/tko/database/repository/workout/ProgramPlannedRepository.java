package tko.database.repository.workout;

import org.springframework.data.jpa.repository.JpaRepository;
import tko.database.entity.workout.ProgramPlannedEntity;

public interface ProgramPlannedRepository extends JpaRepository<ProgramPlannedEntity, Long> {
    boolean existsByPlannedWorkout_Id(Long plannedWorkoutId);
}