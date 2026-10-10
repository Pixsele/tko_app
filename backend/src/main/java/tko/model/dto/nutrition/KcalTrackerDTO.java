package tko.model.dto.nutrition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KcalTrackerDTO {
    private Long id;
    private Long userId;
    private LocalDate date;
    private List<KcalProductDTO> kcalProducts;
}