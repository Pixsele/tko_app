package tko.database.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import tko.database.entity.user.UsersEntity;
import tko.database.entity.user.WeightTrackerEntity;
import tko.model.dto.user.WeightTrackerDTO;

import java.time.LocalDate;
import java.util.List;

public interface WeightTrackerRepository extends JpaRepository<WeightTrackerEntity,Long> {
    boolean existsByDateAndUser_Id(LocalDate date, Long userId);

    List<WeightTrackerEntity> findAllByUser_IdAndDateBetween(Long userId, LocalDate dateAfter, LocalDate dateBefore);
}
