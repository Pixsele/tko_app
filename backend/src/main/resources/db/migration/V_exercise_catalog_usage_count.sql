-- Популярность упражнения: сколько раз оно добавлено в тренировки.
-- Переименуй файл под свою нумерацию Flyway (например V12__exercise_usage_count.sql)
-- или выполни вручную, если используется ddl-auto.

ALTER TABLE exercise ADD COLUMN IF NOT EXISTS usage_count INTEGER NOT NULL DEFAULT 0;

-- Заполняем по уже существующим тренировкам
UPDATE exercise e
SET usage_count = sub.cnt
FROM (
    SELECT exercise_id, COUNT(*) AS cnt
    FROM workout_exercises
    GROUP BY exercise_id
) sub
WHERE sub.exercise_id = e.id;

UPDATE exercise SET like_count = 0 WHERE like_count IS NULL;

CREATE INDEX IF NOT EXISTS idx_exercise_like_count  ON exercise (like_count);
CREATE INDEX IF NOT EXISTS idx_exercise_usage_count ON exercise (usage_count);
CREATE INDEX IF NOT EXISTS idx_exercise_difficult   ON exercise (difficult);
