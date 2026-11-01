package tko.database.repository.nutrition;

import tko.database.entity.nutrition.KcalTrackerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface KcalTrackerRepository extends JpaRepository<KcalTrackerEntity, Long> {
    boolean existsByUser_IdAndDate(Long userId, LocalDate date);
}