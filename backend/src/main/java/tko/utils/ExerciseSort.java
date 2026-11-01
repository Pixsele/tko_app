package tko.utils;

import lombok.Getter;
import org.springframework.data.domain.Sort;

/**
 * Варианты сортировки каталога упражнений.
 * <ul>
 *     <li>RATING — по количеству лайков (likeCount)</li>
 *     <li>POPULARITY — по количеству добавлений в тренировки (usageCount)</li>
 *     <li>NAME — по названию</li>
 *     <li>NEWEST — по дате добавления (id)</li>
 * </ul>
 */
@Getter
public enum ExerciseSort {
    RATING("likeCount", Sort.Direction.DESC),
    POPULARITY("usageCount", Sort.Direction.DESC),
    NAME("name", Sort.Direction.ASC),
    NEWEST("id", Sort.Direction.DESC);

    private final String field;
    private final Sort.Direction defaultDirection;

    ExerciseSort(String field, Sort.Direction defaultDirection) {
        this.field = field;
        this.defaultDirection = defaultDirection;
    }

    /**
     * Строит Sort с дополнительной сортировкой по id,
     * чтобы порядок был стабильным при пагинации (одинаковые лайки и т.п.).
     */
    public Sort toSort(Sort.Direction direction) {
        Sort.Direction dir = direction != null ? direction : defaultDirection;
        if ("id".equals(field)) {
            return Sort.by(dir, "id");
        }
        return Sort.by(dir, field).and(Sort.by(Sort.Direction.ASC, "id"));
    }
}
