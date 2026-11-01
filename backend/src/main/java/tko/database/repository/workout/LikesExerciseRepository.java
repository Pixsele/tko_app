package tko.database.repository.workout;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tko.database.entity.user.UsersEntity;
import tko.database.entity.workout.LikesExerciseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikesExerciseRepository extends JpaRepository<LikesExerciseEntity, Long> {
    List<LikesExerciseEntity> findAllByExercise_Id(Long exerciseId);

    Page<LikesExerciseEntity> findByUser_Id(Long userId, Pageable pageable);

    boolean existsByUser_IdAndExercise_Id(Long userId, Long exerciseId);

    Long user(UsersEntity user);

    boolean existsByUser_IdAndId(Long userId, Long id);
}
