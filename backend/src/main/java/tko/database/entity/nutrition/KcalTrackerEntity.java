package tko.database.entity.nutrition;

import tko.database.entity.user.UsersEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "kcal_tracker")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KcalTrackerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UsersEntity user;

    private LocalDate date;

    @OneToMany(mappedBy = "kcalTracker", cascade = CascadeType.ALL)
    private List<KcalProductEntity> kcalProducts;
}
