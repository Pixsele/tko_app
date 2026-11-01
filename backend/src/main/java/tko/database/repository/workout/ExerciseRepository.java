package tko.database.repository.workout;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tko.database.entity.workout.ExerciseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciseRepository extends JpaRepository<ExerciseEntity, Long> {

    Page<ExerciseEntity> findAll(Pageable pageable);
}