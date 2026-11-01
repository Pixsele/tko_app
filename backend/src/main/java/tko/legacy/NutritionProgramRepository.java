package tko.legacy;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NutritionProgramRepository extends JpaRepository<NutritionProgramEntity, Long> {
}