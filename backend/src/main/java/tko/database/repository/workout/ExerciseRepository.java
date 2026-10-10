package tko.database.repository.workout;

import tko.database.entity.workout.ExerciseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciseRepository extends JpaRepository<ExerciseEntity, Long> {
}