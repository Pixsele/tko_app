package tko.database.repository.workout;

import org.springframework.data.jpa.repository.JpaRepository;
import tko.database.entity.workout.CurrentTrainingProgramEntity;

import java.util.Optional;

public interface CurrentTrainingProgramRepository extends JpaRepository<CurrentTrainingProgramEntity, Long> {
}