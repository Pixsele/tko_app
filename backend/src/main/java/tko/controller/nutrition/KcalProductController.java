package tko.controller.nutrition;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tko.model.dto.nutrition.KcalProductDTO;
import tko.service.nutrition.KcalProductService;

@RestController
@RequestMapping("/kcal-product")
public class KcalProductController {

    private final KcalProductService kcalProductService;

    @Autowired
    public KcalProductController(KcalProductService kcalProductService) {
        this.kcalProductService = kcalProductService;
    }

    @PostMapping
    public ResponseEntity<KcalProductDTO> create(@Valid @RequestBody KcalProductDTO kcalProductDTO) {
        KcalProductDTO createdKcalProduct = kcalProductService.createKcalProduct(kcalProductDTO);
        return new ResponseEntity<>(createdKcalProduct, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<KcalProductDTO> read(@PathVariable Long id) {
        KcalProductDTO readKcalProduct = kcalProductService.readKcalProductById(id);
        return new ResponseEntity<>(readKcalProduct, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<KcalProductDTO> update(@PathVariable Long id,@Valid @RequestBody KcalProductDTO kcalProductDTO) {
        KcalProductDTO updatedKcalProduct = kcalProductService.updateKcalProduct(id,kcalProductDTO);
        return new ResponseEntity<>(updatedKcalProduct, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<KcalProductDTO> delete(@PathVariable Long id) {
        KcalProductDTO deleteKcalProduct = kcalProductService.deleteKcalProductById(id);
        return new ResponseEntity<>(deleteKcalProduct, HttpStatus.OK);
    }
}
