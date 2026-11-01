package tko.database.specification;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;
import tko.database.entity.workout.ExerciseEntity;
import tko.database.entity.workout.LikesExerciseEntity;
import tko.model.dto.workout.ExerciseCatalogFilter;
import tko.utils.personSVG.Muscle;

import java.util.ArrayList;
import java.util.List;

/**
 * Динамические условия выборки каталога упражнений (JPA Criteria).
 */
public final class ExerciseSpecifications {

    /**
     * Разделитель, которым MuscularConverter склеивает List<Muscle> в строку колонки.
     * ВАЖНО: должен совпадать с тем, что реально пишет конвертер (например "CHEST,TRICEPS").
     * Если в конвертере ", " или ";" — поменять здесь.
     */
    public static final String MUSCLE_SEPARATOR = ",";

    private ExerciseSpecifications() {
    }

    public static Specification<ExerciseEntity> byFilter(ExerciseCatalogFilter f, Long currentUserId) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (f == null) {
                return cb.conjunction();
            }

            // Название
            if (f.getName() != null && !f.getName().isBlank()) {
                String pattern = "%" + escapeLike(f.getName().trim().toLowerCase()) + "%";
                predicates.add(cb.like(cb.lower(root.get("name")), pattern, '\\'));
            }

            // Сложность
            if (f.getDifficulties() != null && !f.getDifficulties().isEmpty()) {
                predicates.add(root.get("difficult").in(f.getDifficulties()));
            }

            // Тип
            if (f.getTypes() != null && !f.getTypes().isEmpty()) {
                predicates.add(root.get("type").in(f.getTypes()));
            }

            // Инвентарь
            if (f.getRequiresEquipment() != null) {
                predicates.add(cb.equal(root.get("requiresEquipment"), f.getRequiresEquipment()));
            }

            // Рейтинг
            if (f.getMinLikes() != null) {
                predicates.add(cb.ge(cb.coalesce(root.get("likeCount"), 0), f.getMinLikes()));
            }

            // Популярность
            if (f.getMinUsage() != null) {
                predicates.add(cb.ge(cb.coalesce(root.get("usageCount"), 0), f.getMinUsage()));
            }

            // Группы мышц.
            // Колонка хранит строку вида "CHEST,TRICEPS" -> оборачиваем в ",CHEST,TRICEPS,"
            // и ищем ",CHEST," — так "ARM" не совпадёт с "FOREARM".
            if (f.getMuscles() != null && !f.getMuscles().isEmpty()) {
                Expression<String> column = root.get("muscularGroup").as(String.class);
                Expression<String> wrapped = cb.concat(cb.concat(MUSCLE_SEPARATOR, column), MUSCLE_SEPARATOR);

                List<Predicate> musclePredicates = new ArrayList<>();
                for (Muscle m : f.getMuscles()) {
                    if (m != null) {
                        musclePredicates.add(cb.like(wrapped, "%" + MUSCLE_SEPARATOR + m.name() + MUSCLE_SEPARATOR + "%"));
                    }
                }
                if (!musclePredicates.isEmpty()) {
                    Predicate[] arr = musclePredicates.toArray(new Predicate[0]);
                    predicates.add(Boolean.TRUE.equals(f.getMatchAllMuscles()) ? cb.and(arr) : cb.or(arr));
                }
            }

            // Только лайкнутые текущим пользователем
            if (Boolean.TRUE.equals(f.getOnlyLiked()) && currentUserId != null) {
                Subquery<Long> sub = query.subquery(Long.class);
                Root<LikesExerciseEntity> like = sub.from(LikesExerciseEntity.class);
                sub.select(like.get("exercise").get("id"))
                        .where(cb.equal(like.get("user").get("id"), currentUserId));
                predicates.add(root.get("id").in(sub));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static String escapeLike(String s) {
        return s.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
    }
}
