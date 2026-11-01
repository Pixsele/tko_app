package tko.database.repository.workout;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tko.database.entity.workout.LikesTrainingsProgramEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikesTrainingsProgramRepository extends JpaRepository<LikesTrainingsProgramEntity, Long> {
    boolean existsByUser_IdAndTrainingsProgram_Id(Long userId, Long trainingsProgramId);

    Page<LikesTrainingsProgramEntity> findByUser_Id(Long userId, Pageable pageable);

    boolean existsByUser_IdAndId(Long userId, Long id);
}
