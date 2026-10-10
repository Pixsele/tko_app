package tko.database.repository.workout;

import tko.database.entity.workout.TrainingsProgramEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingsProgramRepository extends JpaRepository<TrainingsProgramEntity, Long> {
}