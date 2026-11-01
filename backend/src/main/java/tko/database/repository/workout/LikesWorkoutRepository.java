package tko.database.repository.workout;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tko.database.entity.workout.LikesWorkoutEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikesWorkoutRepository extends JpaRepository<LikesWorkoutEntity, Long> {
    boolean existsByUser_IdAndWorkout_Id(Long userId, Long workoutId);

    Page<LikesWorkoutEntity> findByUser_Id(Long userId, Pageable pageable);

    boolean existsByUser_IdAndId(Long userId, Long id);
}