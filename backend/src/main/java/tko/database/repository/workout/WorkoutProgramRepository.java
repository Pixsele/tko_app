package tko.database.repository.workout;

import tko.database.entity.workout.WorkoutProgramEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkoutProgramRepository extends JpaRepository<WorkoutProgramEntity, Long> {
}
