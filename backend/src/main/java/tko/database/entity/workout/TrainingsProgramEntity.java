package tko.database.entity.workout;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "trainings_program")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingsProgramEntity {
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

    @OneToMany(mappedBy = "program", cascade = CascadeType.ALL)
    private List<WorkoutProgramEntity> workoutPrograms;

    @Column(name = "like_count")
    private Integer likeCount;
}
