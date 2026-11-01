package tko.database.entity.workout;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tko.database.entity.user.UsersEntity;

@Entity
@Table(name = "current_training_program")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrentTrainingProgramEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UsersEntity user;

    @ManyToOne
    @JoinColumn(name = "training_program_id", nullable = false)
    private TrainingsProgramEntity trainingProgram;
}