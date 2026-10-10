package tko.database.entity.workout;

import tko.database.entity.user.UsersEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "likes_workout", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "workout_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikesWorkoutEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UsersEntity user;

    @ManyToOne
    @JoinColumn(name = "workout_id")
    private WorkoutEntity workout;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}