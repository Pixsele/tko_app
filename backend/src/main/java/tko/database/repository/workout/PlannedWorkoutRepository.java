package tko.database.repository.workout;

import tko.database.entity.workout.PlannedWorkoutEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PlannedWorkoutRepository extends JpaRepository<PlannedWorkoutEntity, Long> {
    List<PlannedWorkoutEntity> findAllByUser_IdAndDateBetween(Long userId, LocalDate dateAfter, LocalDate dateBefore);
}
