package tko.model.dto.workout;

import lombok.Data;
import lombok.NoArgsConstructor;
import tko.utils.DifficultyLevel;
import tko.utils.ExerciseType;
import tko.utils.personSVG.Muscle;

import java.util.List;

/**
 * Параметры фильтрации каталога упражнений.
 * Биндится из query-параметров через @ModelAttribute:
 * <pre>
 * GET /exercise/catalog?muscles=CHEST&muscles=TRICEPS&difficulties=EASY&minLikes=5
 * </pre>
 * Все поля необязательные: null / пустой список = фильтр не применяется.
 */
@Data
@NoArgsConstructor
public class ExerciseCatalogFilter {

    /** Поиск по подстроке в названии (без учёта регистра). */
    private String name;

    /** Группы мышц. */
    private List<Muscle> muscles;

    /**
     * Режим фильтра по мышцам:
     * false (по умолчанию) — упражнение задействует ХОТЯ БЫ ОДНУ из выбранных групп;
     * true — задействует ВСЕ выбранные группы.
     */
    private Boolean matchAllMuscles = false;

    /** Уровни сложности (OR). */
    private List<DifficultyLevel> difficulties;

    /** Типы упражнений (OR). */
    private List<ExerciseType> types;

    /** Требуется ли инвентарь. */
    private Boolean requiresEquipment;

    /** Минимальный рейтинг (кол-во лайков). */
    private Integer minLikes;

    /** Минимальная популярность (сколько раз добавлено в тренировки). */
    private Integer minUsage;

    /** Показать только упражнения, лайкнутые текущим пользователем. */
    private Boolean onlyLiked = false;
}
