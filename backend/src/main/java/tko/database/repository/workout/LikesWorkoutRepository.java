package tko.database.repository.workout;

import tko.database.entity.workout.LikesWorkoutEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikesWorkoutRepository extends JpaRepository<LikesWorkoutEntity, Long> {
}