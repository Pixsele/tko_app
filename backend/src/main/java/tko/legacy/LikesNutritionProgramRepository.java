package tko.legacy;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikesNutritionProgramRepository extends JpaRepository<LikesNutritionProgramEntity, Long> {
}