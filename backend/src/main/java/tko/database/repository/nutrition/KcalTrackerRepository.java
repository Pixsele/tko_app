package tko.database.repository.nutrition;

import tko.database.entity.nutrition.KcalTrackerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KcalTrackerRepository extends JpaRepository<KcalTrackerEntity, Long> {
}