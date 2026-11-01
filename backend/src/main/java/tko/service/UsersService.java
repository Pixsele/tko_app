package tko.service;


import tko.database.entity.workout.CurrentTrainingProgramEntity;
import tko.database.repository.workout.CurrentTrainingProgramRepository;
import tko.legacy.NutritionProgramEntity;
import tko.database.entity.workout.TrainingsProgramEntity;
import tko.database.entity.user.UsersEntity;
import tko.legacy.NutritionProgramRepository;
import tko.database.repository.workout.TrainingsProgramRepository;
import tko.database.repository.user.UsersRepository;
import tko.model.dto.user.UsersDTO;
import tko.model.mapper.UsersMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UsersService {
    private final UsersRepository usersRepository;
    private final TrainingsProgramRepository trainingsProgramRepository;
    private final NutritionProgramRepository nutritionProgramRepository;
    private final UsersMapper usersMapper;
    private final CurrentTrainingProgramRepository currentTrainingProgramRepository;

    @Autowired
    public UsersService(UsersRepository usersRepository, TrainingsProgramRepository trainingsProgramRepository, NutritionProgramRepository nutritionProgramRepository, UsersMapper usersMapper, CurrentTrainingProgramRepository currentTrainingProgramRepository) {
        this.usersRepository = usersRepository;
        this.trainingsProgramRepository = trainingsProgramRepository;
        this.nutritionProgramRepository = nutritionProgramRepository;
        this.usersMapper = usersMapper;
        this.currentTrainingProgramRepository = currentTrainingProgramRepository;
    }

    public UsersDTO create(UsersDTO usersDTO) {
        UsersEntity usersEntity = usersMapper.toEntity(usersDTO);
        usersRepository.save(usersEntity);
        return usersMapper.toDTO(usersEntity);
    }

    public UsersDTO getUser(Long id) {
        //special for Dima( ˘ ³˘)♥︎
        Objects.requireNonNull(id);

        UsersEntity usersEntity = usersRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("User not found"));
        UsersDTO usersDTO= usersMapper.toDTO(usersEntity);
        UsersEntity usersEntity1 = usersMapper.toEntity(usersDTO);
        System.out.println(usersEntity1);
        return usersDTO;
    }

    public UsersDTO updateWeight(Double weight, Long userId) {
        Objects.requireNonNull(weight);
        Objects.requireNonNull(userId);

        UsersEntity usersEntity = usersRepository.findById(userId).orElseThrow(()-> new EntityNotFoundException("User not found"));
        usersEntity.setWeight(weight);
        usersRepository.save(usersEntity);
        return usersMapper.toDTO(usersEntity);
    }

    public UsersDTO updateUser(Long id, UsersDTO usersDTO) {
        Objects.requireNonNull(id);

        UsersEntity usersEntity = usersRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("User not found"));

        if(usersDTO.getName()!=null) {
            usersEntity.setName(usersDTO.getName());
        }
        if (usersDTO.getLogin()!=null) {
            usersEntity.setLogin(usersDTO.getLogin());
        }
        if(usersDTO.getBirthDay()!=null){
            usersEntity.setBirthDay(usersDTO.getBirthDay());
        }
        if(usersDTO.getWeight()!=null){
            usersEntity.setWeight(usersDTO.getWeight());
        }
        if(usersDTO.getHeight()!=null){
            usersEntity.setHeight(usersDTO.getHeight());
        }
        if(usersDTO.getTargetKcal()!=null){
            usersEntity.setTargetKcal(usersDTO.getTargetKcal());
        }
        if(usersDTO.getCreatedAt()!=null){
            usersEntity.setCreatedAt(usersDTO.getCreatedAt());
        }

        if(usersDTO.getCurrentTrainingProgramId() != null){
           CurrentTrainingProgramEntity currentTrainingProgramEntity = currentTrainingProgramRepository.findById(usersDTO
                    .getCurrentTrainingProgramId()).orElseThrow(()-> new EntityNotFoundException("Training program not found"));

            usersEntity.setCurrentTrainingProgram(currentTrainingProgramEntity);
        }

        if(usersDTO.getCurrentNutritionProgramId() != null){
            NutritionProgramEntity nutritionProgram = nutritionProgramRepository.findById(usersDTO
                    .getCurrentNutritionProgramId()).orElseThrow(()-> new EntityNotFoundException("Nutrition program not found"));
        }

        UsersEntity savedUser = usersRepository.save(usersEntity);

        return usersMapper.toDTO(savedUser);
    }

    public UsersDTO deleteUser(Long id) {
        if(id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        UsersEntity usersEntity = usersRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("User not found"));
        usersRepository.delete(usersEntity);
        return usersMapper.toDTO(usersEntity);
    }
}
