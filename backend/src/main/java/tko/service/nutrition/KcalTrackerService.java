package tko.service.nutrition;


import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tko.database.entity.nutrition.KcalTrackerEntity;
import tko.database.entity.user.UsersEntity;
import tko.database.repository.nutrition.KcalTrackerRepository;
import tko.database.repository.user.UsersRepository;
import tko.model.dto.nutrition.KcalTrackerDTO;
import tko.model.mapper.nutrition.KcalTrackerMapper;

import java.util.Objects;

@Service
public class KcalTrackerService {

    private final KcalTrackerRepository kcalTrackerRepository;
    private final UsersRepository usersRepository;
    private final KcalTrackerMapper kcalTrackerMapper;


    @Autowired
    public KcalTrackerService(KcalTrackerRepository kcalTrackerRepository, UsersRepository usersRepository, KcalTrackerMapper kcalTrackerMapper) {
        this.kcalTrackerRepository = kcalTrackerRepository;
        this.usersRepository = usersRepository;
        this.kcalTrackerMapper = kcalTrackerMapper;
    }

    public KcalTrackerDTO createKcalTracker(KcalTrackerDTO kcalTrackerDTO) {
        if(kcalTrackerDTO.getId() != null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id must be null");
        }

        if(!(usersRepository.existsById(kcalTrackerDTO.getUserId()))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        if(!(Objects.equals(SecurityContextHolder.getContext().getAuthentication().getName(), usersRepository.findById(kcalTrackerDTO.getUserId()).get().getLogin()))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");

        }

        KcalTrackerEntity kcalTrackerEntity = kcalTrackerMapper.toEntity(kcalTrackerDTO);
        kcalTrackerRepository.save(kcalTrackerEntity);
        return kcalTrackerMapper.toDto(kcalTrackerEntity);
    }

    public KcalTrackerDTO readKcalTracker(Long id) {
        if(id == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id must not be null");
        }

        KcalTrackerEntity kcalTrackerEntity = kcalTrackerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Id not found"));
        return kcalTrackerMapper.toDto(kcalTrackerEntity);
    }

    public KcalTrackerDTO updateKcalTracker(Long id,KcalTrackerDTO kcalTrackerDTO) {
        if(id == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id must not be null");
        }

        UsersEntity usersEntity = usersRepository.findById(kcalTrackerDTO.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist"));

        if(kcalTrackerRepository.existsByUser_IdAndDate(kcalTrackerDTO.getUserId(), kcalTrackerDTO.getDate())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Kcal tracker already exists by this user and date");
        }

        KcalTrackerEntity kcalTrackerEntity = kcalTrackerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Id not found"));

        kcalTrackerMapper.updateEntity(kcalTrackerDTO, kcalTrackerEntity);
        kcalTrackerRepository.save(kcalTrackerEntity);
        return kcalTrackerMapper.toDto(kcalTrackerEntity);
    }

    public KcalTrackerDTO deleteKcalTracker(Long id) {
        if(id == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id must not be null");
        }

        KcalTrackerEntity deleteKcalTracker = kcalTrackerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Id not found"));

        //TODO Check dependencies to delete

        kcalTrackerRepository.delete(deleteKcalTracker);
        return kcalTrackerMapper.toDto(deleteKcalTracker);
    }
}
