package tko.database.repository.nutrition;

import tko.database.entity.nutrition.NutritionProgramEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NutritionProgramRepository extends JpaRepository<NutritionProgramEntity, Long> {
}