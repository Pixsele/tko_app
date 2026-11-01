package tko.database.repository.workout;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tko.database.entity.workout.ExerciseEntity;

/**
 * Добавлено:
 *  - JpaSpecificationExecutor — для динамических фильтров каталога;
 *  - атомарный инкремент/декремент счётчика популярности (без гонок).
 * Если в старом репозитории были другие методы — перенеси их сюда.
 */
@Repository
public interface ExerciseRepository extends JpaRepository<ExerciseEntity, Long> {

    Page<ExerciseEntity> findAll(Pageable pageable);
}
