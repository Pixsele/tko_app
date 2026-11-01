package tko.service.nutrition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tko.database.entity.nutrition.ProductEntity;
import tko.database.entity.user.UsersEntity;
import tko.database.repository.nutrition.ProductRepository;
import tko.database.repository.user.UsersRepository;
import tko.model.dto.nutrition.ProductDTO;
import tko.model.dto.nutrition.ProductForPageDTO;
import tko.model.mapper.nutrition.ProductMapper;
import tko.utils.KcalHelper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final UsersRepository usersRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, ProductMapper productMapper, UsersRepository usersRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.usersRepository = usersRepository;
    }

    public ProductDTO createProduct(ProductDTO productDTO) {
        if(productDTO.getId() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Id must be null");
        }

        if(productDTO.getCreatedAt() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CreatedAt must be null");
        }

        productDTO.setCreatedAt(LocalDateTime.now());

        ProductEntity productEntity = productMapper.toEntity(productDTO);
        productRepository.save(productEntity);
        return productMapper.toDto(productEntity);
    }

    public ProductDTO readProduct(Long id) {
        if(id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id must not be null");
        }

        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        return productMapper.toDto(productEntity);
    }

    public ProductDTO updateProduct(Long id,ProductDTO productDTO) {
        if(id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id must not be null");
        }

        if(productDTO.getId() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Id must be null");
        }

        if(productDTO.getCreatedAt() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CreatedAt must be null");
        }

        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        productMapper.updateEntity(productDTO, productEntity);
        productRepository.save(productEntity);
        return productMapper.toDto(productEntity);
    }

    public ProductDTO deleteProduct(Long id) {
        if(id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id must not be null");
        }

        ProductEntity deletedProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        //TODO Check to delete

        productRepository.delete(deletedProduct);
        return productMapper.toDto(deletedProduct);
    }

    public List<ProductForPageDTO> searchProducts(String keyword) {
        List<ProductDTO> productDTOList = productRepository.findByNameContainingIgnoreCase(keyword);

        UsersEntity user = usersRepository.findByLogin(SecurityContextHolder.getContext().getAuthentication().getName());

        Integer target = user.getTargetKcal();

        List<ProductForPageDTO> productForPageDTOList = new ArrayList<>();

        for(ProductDTO productDTO : productDTOList) {
            ProductForPageDTO productForPageDTO = new ProductForPageDTO();

            productForPageDTO.setId(productDTO.getId());
            productForPageDTO.setName(productDTO.getName());
            productForPageDTO.setCalories(productDTO.getKcal());
            productForPageDTO.setUnit(productDTO.getUnit());
            productForPageDTO.setPortion(productDTO.getPortion());
            productForPageDTO.setPercentOfTarget(KcalHelper.percentOfTarget(target, productDTO.getKcal()));

            productForPageDTOList.add(productForPageDTO);
        }

        return productForPageDTOList;
    }
}
