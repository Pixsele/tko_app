package tko.model.dto.workout;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Доступные значения для панели фильтров Android-приложения.
 * Клиент строит чипы/чекбоксы из этих списков, а не хардкодит enum'ы.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseFilterOptionsDTO {
    private List<String> muscles;
    private List<String> difficulties;
    private List<String> types;
    private List<String> sortOptions;
    private List<String> directions;
}
