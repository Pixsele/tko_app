package tko.database.entity.workout;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "workout")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "workoutExercises")
public class WorkoutEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private String name;
    private String type;
    private String difficult;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "workout", cascade = CascadeType.ALL)
    private List<WorkoutExerciseEntity> workoutExercises;

    @Column(name = "like_count")
    private Integer likeCount;
}