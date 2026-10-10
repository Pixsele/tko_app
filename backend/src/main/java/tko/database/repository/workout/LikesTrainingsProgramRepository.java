package tko.database.repository.workout;

import tko.database.entity.workout.LikesTrainingsProgramEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikesTrainingsProgramRepository extends JpaRepository<LikesTrainingsProgramEntity, Long> {
}
