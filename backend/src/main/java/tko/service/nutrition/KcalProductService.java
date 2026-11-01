package tko.service.nutrition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tko.database.entity.nutrition.KcalProductEntity;
import tko.database.repository.nutrition.KcalProductRepository;
import tko.database.repository.nutrition.KcalTrackerRepository;
import tko.database.repository.nutrition.ProductRepository;
import tko.model.dto.nutrition.KcalProductDTO;
import tko.model.mapper.nutrition.KcalProductMapper;

@Service
public class KcalProductService {

    private final KcalProductRepository kcalProductRepository;
    private final KcalProductMapper kcalProductMapper;
    private final KcalTrackerRepository kcalTrackerRepository;
    private final ProductRepository productRepository;

    @Autowired
    public KcalProductService(KcalProductRepository repository, KcalProductMapper kcalProductMapper, KcalTrackerRepository kcalTrackerRepository, ProductRepository productRepository) {
        this.kcalProductRepository = repository;
        this.kcalProductMapper = kcalProductMapper;
        this.kcalTrackerRepository = kcalTrackerRepository;
        this.productRepository = productRepository;
    }

    public KcalProductDTO createKcalProduct(KcalProductDTO kcalProductDTO) {
        if(kcalProductDTO.getId() != null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id must be null");
        }

        if(!(kcalTrackerRepository.existsById(kcalProductDTO.getKcalTrackerId()))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "KcalTracker not found");
        }

        if(!(productRepository.existsById(kcalProductDTO.getProductId()))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }

        KcalProductEntity kcalProductEntity = kcalProductMapper.toEntity(kcalProductDTO);
        kcalProductRepository.save(kcalProductEntity);
        return kcalProductMapper.toDto(kcalProductEntity);
    }

    public KcalProductDTO readKcalProductById(Long id) {
        if(id == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id must not be null");
        }

        KcalProductEntity kcalProductEntity = kcalProductRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist"));

        return kcalProductMapper.toDto(kcalProductEntity);
    }

    public KcalProductDTO updateKcalProduct(Long id, KcalProductDTO kcalProductDTO) {
        if(id == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id must not be null");
        }

        if(!(kcalTrackerRepository.existsById(kcalProductDTO.getKcalTrackerId()))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "KcalTracker not found");
        }

        if(!(productRepository.existsById(kcalProductDTO.getProductId()))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }

        KcalProductEntity kcalProductEntity = kcalProductRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "KcalProduct does not exist"));

        kcalProductMapper.updateEntity(kcalProductDTO, kcalProductEntity);
        return kcalProductMapper.toDto(kcalProductEntity);
    }

    public KcalProductDTO deleteKcalProductById(Long id) {
        if(id == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id must not be null");
        }

        KcalProductEntity kcalProductEntity = kcalProductRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist"));

        if(kcalTrackerRepository.existsById(kcalProductEntity.getId())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "KcalTracker cannot be deleted");
        }

        kcalProductRepository.delete(kcalProductEntity);
        return kcalProductMapper.toDto(kcalProductEntity);
    }
}
