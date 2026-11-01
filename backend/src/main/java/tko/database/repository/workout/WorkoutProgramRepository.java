package tko.database.repository.workout;

import tko.database.entity.workout.WorkoutProgramEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkoutProgramRepository extends JpaRepository<WorkoutProgramEntity, Long> {
    List<WorkoutProgramEntity> findAllByProgram_Id(Long programId);
}
