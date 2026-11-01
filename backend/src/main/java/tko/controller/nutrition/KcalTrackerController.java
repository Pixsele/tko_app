package tko.controller.nutrition;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tko.model.dto.nutrition.KcalTrackerDTO;
import tko.service.nutrition.KcalTrackerService;

@RestController
@RequestMapping("/kcal-tracker")
public class KcalTrackerController {

    private final KcalTrackerService kcalTrackerService;

    @Autowired
    public KcalTrackerController(KcalTrackerService kcalTrackerService) {
        this.kcalTrackerService = kcalTrackerService;
    }

    @PostMapping
    public ResponseEntity<KcalTrackerDTO> createTracker(@Valid @RequestBody KcalTrackerDTO kcalTrackerDTO) {
        KcalTrackerDTO trackerDTO = kcalTrackerService.createKcalTracker(kcalTrackerDTO);
        return new ResponseEntity<>(trackerDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<KcalTrackerDTO> readTracker(@PathVariable Long id) {
        KcalTrackerDTO trackerDTO = kcalTrackerService.readKcalTracker(id);
        return new ResponseEntity<>(trackerDTO, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<KcalTrackerDTO> updateTracker(@PathVariable Long id ,@Valid @RequestBody KcalTrackerDTO kcalTrackerDTO) {
        KcalTrackerDTO trackerDTO = kcalTrackerService.updateKcalTracker(id, kcalTrackerDTO);
        return new ResponseEntity<>(trackerDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<KcalTrackerDTO> deleteTracker(@PathVariable Long id) {
        KcalTrackerDTO trackerDTO = kcalTrackerService.deleteKcalTracker(id);
        return new ResponseEntity<>(trackerDTO, HttpStatus.OK);
    }
}
