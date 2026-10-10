package tko.database.repository.nutrition;

import tko.database.entity.nutrition.LikesProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikesProductRepository extends JpaRepository<LikesProductEntity, Long> {
}