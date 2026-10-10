package tko.database.repository.workout;

import tko.database.entity.workout.PlannedWorkoutEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlannedWorkoutRepository extends JpaRepository<PlannedWorkoutEntity, Long> {
}
