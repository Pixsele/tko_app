package tko.database.repository.workout;


import tko.database.entity.workout.WorkoutEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkoutRepository extends JpaRepository<WorkoutEntity, Long> {

}