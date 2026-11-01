package tko.controller.nutrition;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tko.model.dto.nutrition.ProductDTO;
import tko.model.dto.nutrition.ProductForPageDTO;
import tko.service.nutrition.ProductService;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> readProduct(@PathVariable Long id) {
        ProductDTO readProductDto = productService.readProduct(id);
        return new ResponseEntity<>(readProductDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        ProductDTO createdProduct = productService.createProduct(productDTO);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDTO productDTO) {
        ProductDTO updatedProduct = productService.updateProduct(id, productDTO);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long id) {
        ProductDTO deletedProduct = productService.deleteProduct(id);
        return new ResponseEntity<>(deletedProduct, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductForPageDTO>> searchProduct(@RequestParam String keyword) {
        List<ProductForPageDTO> productForPageDTOList = productService.searchProducts(keyword);
        return new ResponseEntity<>(productForPageDTOList, HttpStatus.OK);
    }
}
