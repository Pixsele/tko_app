package tko.database.entity.workout;

import tko.database.entity.user.UsersEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "likes_exercise", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "exercise_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikesExerciseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UsersEntity user;

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private ExerciseEntity exercise;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
