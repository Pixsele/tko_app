package tko.database.entity.nutrition;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "kcal_product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KcalProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "kcal_tracker_id")
    private KcalTrackerEntity kcalTracker;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    private int count;
    private String typeMeal;
    private LocalDate date;
}
