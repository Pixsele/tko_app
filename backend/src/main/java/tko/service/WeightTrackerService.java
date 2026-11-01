package tko.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tko.database.entity.user.UsersEntity;
import tko.database.entity.user.WeightTrackerEntity;
import tko.database.repository.user.UsersRepository;
import tko.database.repository.user.WeightTrackerRepository;
import tko.model.dto.user.WeightTrackerDTO;
import tko.model.mapper.WeightTrackerMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class WeightTrackerService {
    private final WeightTrackerRepository weightTrackerRepository;
    private final UsersRepository usersRepository;
    private final WeightTrackerMapper weightTrackerMapper;
    private final UsersService usersService;

    @Autowired
    public WeightTrackerService(WeightTrackerRepository weightTrackerRepository, UsersRepository usersRepository, WeightTrackerMapper weightTrackerMapper, UsersService usersService) {
        this.weightTrackerRepository = weightTrackerRepository;
        this.usersRepository = usersRepository;
        this.weightTrackerMapper = weightTrackerMapper;
        this.usersService = usersService;
    }

    public WeightTrackerDTO create(WeightTrackerDTO weightTrackerDTO) {
        if(weightTrackerDTO.getId() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Id must be null");
        }

        if(!(usersRepository.existsById(weightTrackerDTO.getUserId()))){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist");
        }

        if(weightTrackerRepository.existsByDateAndUser_Id(weightTrackerDTO.getDate(), weightTrackerDTO.getUserId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Weight tracker already exists by this date");
        }

        if(!Objects.equals(SecurityContextHolder.getContext().getAuthentication().getName(),usersRepository.findById(weightTrackerDTO.getUserId()).get().getLogin() )) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access denied");
        }


        WeightTrackerEntity weightTrackerEntity = weightTrackerMapper.toEntity(weightTrackerDTO);
        weightTrackerRepository.save(weightTrackerEntity);

        if(Objects.equals(weightTrackerDTO.getDate(), LocalDate.now())) {
            usersService.updateWeight(weightTrackerDTO.getWeight(), weightTrackerDTO.getUserId());
        }

        return weightTrackerMapper.toDto(weightTrackerEntity);
    }

    public WeightTrackerDTO read(Long id) {
        if(id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id must not be null");
        }
        WeightTrackerEntity weightTrackerEntity = weightTrackerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id not found"));
        return weightTrackerMapper.toDto(weightTrackerEntity);
    }

    public WeightTrackerDTO update(Long id,WeightTrackerDTO weightTrackerDTO) {
        if(id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id must not be null");
        }

        UsersEntity usersEntity = usersRepository.findById(weightTrackerDTO.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist"));

        if(weightTrackerRepository.existsByDateAndUser_Id(weightTrackerDTO.getDate(), weightTrackerDTO.getUserId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Weight tracker already exists by this date");
        }

        WeightTrackerEntity weightTrackerEntity = weightTrackerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Id not found"));

        weightTrackerEntity.setWeight(weightTrackerDTO.getWeight());
        weightTrackerEntity.setDate(weightTrackerDTO.getDate());
        weightTrackerEntity.setUser(usersEntity);
        WeightTrackerEntity updatedWeightTrackerEntity = weightTrackerRepository.save(weightTrackerEntity);
        return weightTrackerMapper.toDto(updatedWeightTrackerEntity);
    }

    public WeightTrackerDTO delete(Long id) {
        if(id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id must not be null");
        }

        WeightTrackerEntity weightTrackerEntity = weightTrackerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Id not found"));

        weightTrackerRepository.delete(weightTrackerEntity);
        return weightTrackerMapper.toDto(weightTrackerEntity);
    }

    public List<WeightTrackerDTO> readByDate(Long userId,LocalDate startDate,LocalDate endDate) {
        if(userId == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "UserId must not be null");
        }

        if(!(usersRepository.existsById(userId))){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist");
        }

        if (startDate == null || endDate == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start date and end date must not be null");
        }

        List<WeightTrackerEntity> weightTrackerEntityListList = weightTrackerRepository.findAllByUser_IdAndDateBetween(userId, startDate, endDate);

        List<WeightTrackerDTO> weightTrackerDTOList = weightTrackerEntityListList.stream()
                .map(entity -> new WeightTrackerDTO(
                        entity.getId(),
                        entity.getUser().getId(),
                        entity.getWeight(),
                        entity.getDate()
                ))
                .collect(Collectors.toList());

        if(weightTrackerDTOList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No weight records found for the specified period");
        }

        return weightTrackerDTOList;
    }
}
