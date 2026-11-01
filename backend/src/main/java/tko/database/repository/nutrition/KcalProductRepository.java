package tko.database.repository.nutrition;

import tko.database.entity.nutrition.KcalProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KcalProductRepository extends JpaRepository<KcalProductEntity, Long> {
    boolean existsByKcalTracker_IdAndProduct_Id(Long kcalTrackerId, Long productId);
}