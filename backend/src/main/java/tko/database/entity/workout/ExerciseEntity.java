package tko.database.entity.workout;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tko.utils.DifficultyLevel;
import tko.utils.ExerciseType;
import tko.utils.personSVG.Muscle;
import tko.utils.personSVG.MuscularConverter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "exercise", indexes = {
        @Index(name = "idx_exercise_like_count", columnList = "like_count"),
        @Index(name = "idx_exercise_usage_count", columnList = "usage_count"),
        @Index(name = "idx_exercise_difficult", columnList = "difficult")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String instruction;
    private String name;

    @Enumerated(EnumType.STRING)
    private ExerciseType type;

    @Enumerated(EnumType.STRING)
    private DifficultyLevel difficult;

    private Boolean requiresEquipment;

    @Convert(converter = MuscularConverter.class)
    private List<Muscle> muscularGroup;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "video_url")
    private String videoUrl;

    /** Рейтинг. */
    @Column(name = "like_count")
    private Integer likeCount;

    /** Популярность: сколько раз упражнение добавлено в тренировки. */
    @Column(name = "usage_count", nullable = false, columnDefinition = "integer default 0")
    private Integer usageCount = 0;

    @Column(name = "image_updated")
    private LocalDateTime imageUpdated;

    @Column(name = "video_updated")
    private LocalDateTime videoUpdated;
}
