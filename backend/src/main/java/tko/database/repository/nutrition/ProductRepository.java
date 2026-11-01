package tko.database.repository.nutrition;

import tko.database.entity.nutrition.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tko.model.dto.nutrition.ProductDTO;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    List<ProductDTO> findByNameContainingIgnoreCase(String name);
}
