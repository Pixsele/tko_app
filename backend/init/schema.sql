CREATE TABLE trainings_program (
                                   id SERIAL PRIMARY KEY,
                                   name VARCHAR(255) NOT NULL,
                                   description TEXT,
                                   difficult VARCHAR(50) NOT NULL,
                                   created_by VARCHAR(255),
                                   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   like_count INT DEFAULT 0
);

CREATE TABLE nutrition_program (
                                   id SERIAL PRIMARY KEY,
                                   description TEXT,
                                   name VARCHAR(255) NOT NULL,
                                   type VARCHAR(100) NOT NULL,
                                   difficulty VARCHAR(50),
                                   created_by VARCHAR(255),
                                   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   like_count INT DEFAULT 0
);

CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       login VARCHAR(255) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       birth_day DATE NOT NULL,
                       weight DOUBLE PRECISION NOT NULL,
                       height DOUBLE PRECISION NOT NULL,
                       target_kcal INT NOT NULL,
                       gender VARCHAR(10) CHECK (gender IN ('MALE', 'FEMALE')),
                       role VARCHAR(255) CHECK ( role IN('ROLE_ADMIN','ROLE_USER')),
                       current_training_program_id INT REFERENCES trainings_program(id) ON DELETE SET NULL,
                       current_nutrition_program_id INT REFERENCES nutrition_program(id) ON DELETE SET NULL,
                       photo_url VARCHAR(255),
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE kcal_tracker (
                              id SERIAL PRIMARY KEY,
                              user_id BIGINT NOT NULL,
                              date DATE NOT NULL,
                              FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE weight_tracker (
                                id SERIAL PRIMARY KEY,
                                user_id BIGINT NOT NULL,
                                date DATE NOT NULL,
                                weight DOUBLE PRECISION NOT NULL,
                                FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE product (
                         id SERIAL PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         kcal INT NOT NULL,
                         unit VARCHAR(50) NOT NULL,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         fats DECIMAL(10, 2) NOT NULL,
                         carbs DECIMAL(10, 2) NOT NULL,
                         proteins DECIMAL(10, 2) NOT NULL,
                         grams DECIMAL(10, 2) NOT NULL,
                         portion DECIMAL(10, 2) NOT NULL
);

CREATE TABLE kcal_product (
                              id SERIAL PRIMARY KEY,
                              kcal_tracker_id BIGINT NOT NULL,
                              product_id BIGINT NOT NULL,
                              count INT NOT NULL,
                              type_meal VARCHAR(15) CHECK ( type_meal in ('BREAKFAST','LUNCH','DINNER','SNACK')),
                              FOREIGN KEY (kcal_tracker_id) REFERENCES kcal_tracker(id) ON DELETE CASCADE,
                              FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE
);

CREATE TABLE exercise (
                          id SERIAL PRIMARY KEY,
                          instruction TEXT NOT NULL,
                          name VARCHAR(255) NOT NULL,
                          difficult VARCHAR(10) CHECK (difficult IN ('EASY', 'MEDIUM', 'HARD')),
                          requires_equipment BOOLEAN NOT NULL DEFAULT FALSE,
                          muscular_group VARCHAR(255),
                          photo_url VARCHAR(255),
                          video_url VARCHAR(255),
                          type VARCHAR(50) CHECK (type IN ('STRENGTH', 'CARDIO', 'FLEXIBILITY', 'BALANCE', 'ENDURANCE')),
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          like_count INT DEFAULT 0
);

CREATE TABLE workout (
                         id SERIAL PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         description TEXT,
                         difficult VARCHAR(10) CHECK (difficult IN ('EASY', 'MEDIUM', 'HARD')),
                         created_by VARCHAR(255),
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         like_count INT DEFAULT 0
);

CREATE TABLE workout_exercises (
                                   id SERIAL PRIMARY KEY,
                                   workout_id BIGINT NOT NULL,
                                   exercise_id BIGINT NOT NULL,
                                   sets INT NOT NULL,
                                   reps INT NOT NULL,
                                   distance DOUBLE PRECISION,
                                   duration DOUBLE PRECISION,
                                   rest_time INT NOT NULL,
                                   exercise_order INT NOT NULL,
                                   UNIQUE (workout_id, exercise_order),
                                   FOREIGN KEY (workout_id) REFERENCES workout(id) ON DELETE CASCADE,
                                   FOREIGN KEY (exercise_id) REFERENCES exercise(id) ON DELETE CASCADE
);

CREATE TABLE planned_workout (
                                 id SERIAL PRIMARY KEY,
                                 user_id BIGINT NOT NULL,
                                 workout_id BIGINT NOT NULL,
                                 date DATE NOT NULL,
                                 status VARCHAR(20) CHECK (status IN ('PLANNED', 'COMPLETED', 'SKIPPED')),
                                 FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                                 FOREIGN KEY (workout_id) REFERENCES workout(id) ON DELETE CASCADE
);

CREATE TABLE workout_program (
                                 id SERIAL PRIMARY KEY,
                                 program_id BIGINT NOT NULL,
                                 workout_id BIGINT NOT NULL,
                                 workout_order INT NOT NULL,
                                 FOREIGN KEY (program_id) REFERENCES trainings_program(id) ON DELETE CASCADE,
                                 FOREIGN KEY (workout_id) REFERENCES workout(id) ON DELETE CASCADE
);

CREATE TABLE nutrition_plan_product (
                                        id SERIAL PRIMARY KEY,
                                        program_id INT NOT NULL,
                                        product_id INT NOT NULL,
                                        count INT NOT NULL CHECK (count > 0),
                                        type_meal VARCHAR(100) NOT NULL,
                                        date DATE NOT NULL,
                                        FOREIGN KEY (program_id) REFERENCES nutrition_program(id) ON DELETE CASCADE,
                                        FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE
);

CREATE TABLE likes_trainings_program (
                                         id SERIAL PRIMARY KEY,
                                         user_id INT NOT NULL,
                                         trainings_program_id INT NOT NULL,
                                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                         FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                                         FOREIGN KEY (trainings_program_id) REFERENCES trainings_program(id) ON DELETE CASCADE,
                                         UNIQUE (user_id, trainings_program_id)
);

CREATE TABLE likes_nutrition_program (
                                         id SERIAL PRIMARY KEY,
                                         user_id INT NOT NULL,
                                         nutrition_program_id INT NOT NULL,
                                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                         FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                                         FOREIGN KEY (nutrition_program_id) REFERENCES nutrition_program(id) ON DELETE CASCADE,
                                         UNIQUE (user_id, nutrition_program_id)
);

CREATE TABLE likes_product (
                               id SERIAL PRIMARY KEY,
                               user_id INT NOT NULL,
                               product_id INT NOT NULL,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                               FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE,
                               UNIQUE (user_id, product_id)
);

CREATE TABLE likes_exercise (
                                id SERIAL PRIMARY KEY,
                                user_id INT NOT NULL,
                                exercise_id INT NOT NULL,
                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                                FOREIGN KEY (exercise_id) REFERENCES exercise(id) ON DELETE CASCADE,
                                UNIQUE (user_id, exercise_id)
);

CREATE TABLE likes_workout (
                               id SERIAL PRIMARY KEY,
                               user_id INT NOT NULL,
                               workout_id INT NOT NULL,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                               FOREIGN KEY (workout_id) REFERENCES workout(id) ON DELETE CASCADE,
                               UNIQUE (user_id, workout_id)
);