package tko.controller.user;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tko.model.dto.user.WeightTrackerDTO;
import tko.service.WeightTrackerService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/weight")
public class WeightTrackerController {

    private final WeightTrackerService weightTrackerService;

    @Autowired
    public WeightTrackerController(WeightTrackerService weightTrackerService) {
        this.weightTrackerService = weightTrackerService;
    }

    @PostMapping
    public ResponseEntity<WeightTrackerDTO> createWeightTracker(@Valid @RequestBody WeightTrackerDTO weightTrackerDTO) {
        WeightTrackerDTO createdWeight = weightTrackerService.create(weightTrackerDTO);
        return new ResponseEntity<>(createdWeight, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WeightTrackerDTO> readWeightTracker(@PathVariable Long id) {
        WeightTrackerDTO readWeightTracker = weightTrackerService.read(id);
        return new ResponseEntity<>(readWeightTracker, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WeightTrackerDTO> updateWeightTracker(@PathVariable Long id, @Valid @RequestBody WeightTrackerDTO weightTrackerDTO) {
        WeightTrackerDTO updatedWeight = weightTrackerService.update(id, weightTrackerDTO);
        return new ResponseEntity<>(updatedWeight, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<WeightTrackerDTO> deleteWeightTracker(@PathVariable Long id) {
        WeightTrackerDTO deletedWeight = weightTrackerService.delete(id);
        return new ResponseEntity<>(deletedWeight, HttpStatus.OK);
    }

    @GetMapping("/list-by-dates/{id}")
    public ResponseEntity<List<WeightTrackerDTO>> readWeightTrackerByDate(@PathVariable Long id , @RequestParam LocalDate from, @RequestParam LocalDate to) {
        List<WeightTrackerDTO> weightTrackerDTOList = weightTrackerService.readByDate(id,from,to);
        return new ResponseEntity<>(weightTrackerDTOList, HttpStatus.OK);
    }
}
