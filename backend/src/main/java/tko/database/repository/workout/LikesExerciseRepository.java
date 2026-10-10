package tko.database.repository.workout;

import tko.database.entity.workout.LikesExerciseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikesExerciseRepository extends JpaRepository<LikesExerciseEntity, Long> {
}
