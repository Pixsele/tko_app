package tko.model.dto.workout;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tko.utils.DifficultyLevel;
import tko.utils.ExerciseType;
import tko.utils.personSVG.Muscle;

import java.util.List;

/**
 * Карточка упражнения в каталоге.
 * Расширена полями, нужными Android-клиенту для отображения
 * результата фильтрации и сортировки (сложность, мышцы, популярность).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseForPageDTO {
    private Long id;
    private String name;

    private ExerciseType type;
    private DifficultyLevel difficult;
    private Boolean requiresEquipment;
    private List<Muscle> muscularGroup;

    /** Рейтинг — количество лайков. */
    private Integer likeCount;

    /** Популярность — сколько раз упражнение добавлено в тренировки. */
    private Integer usageCount;

    /** Лайкнул ли текущий пользователь. */
    private Boolean liked;

    /** Есть ли картинка (клиент тянет её через GET /exercise/image/{id}). */
    private Boolean hasImage;
}
