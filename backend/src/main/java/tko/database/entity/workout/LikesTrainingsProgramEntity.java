package tko.database.entity.workout;

import tko.database.entity.user.UsersEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "likes_trainings_program", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "trainings_program_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikesTrainingsProgramEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UsersEntity user;

    @ManyToOne
    @JoinColumn(name = "trainings_program_id")
    private TrainingsProgramEntity trainingsProgram;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
