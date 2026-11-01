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
public interface ExerciseRepository extends JpaRepository<ExerciseEntity, Long>,
        JpaSpecificationExecutor<ExerciseEntity> {

    @Modifying(flushAutomatically = true)
    @Query("update ExerciseEntity e set e.usageCount = coalesce(e.usageCount, 0) + 1 where e.id = :id")
    int incrementUsageCount(@Param("id") Long id);

    @Modifying(flushAutomatically = true)
    @Query("update ExerciseEntity e set e.usageCount = " +
            "case when coalesce(e.usageCount, 0) > 0 then e.usageCount - 1 else 0 end " +
            "where e.id = :id")
    int decrementUsageCount(@Param("id") Long id);
}
