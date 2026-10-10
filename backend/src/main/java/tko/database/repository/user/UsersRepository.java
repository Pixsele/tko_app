package tko.database.repository.user;

import tko.database.entity.user.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<UsersEntity, Long> {
    boolean existsByLogin(String login);

    UsersEntity findByLogin(String login);
}
