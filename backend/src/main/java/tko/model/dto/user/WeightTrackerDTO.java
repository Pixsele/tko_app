package tko.model.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeightTrackerDTO {
    private Long id;
    @NotNull
    private Long userId;
    @NotNull
    private Double weight;
    @NotNull
    private LocalDate date;
}
