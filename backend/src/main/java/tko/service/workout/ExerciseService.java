package tko.service.workout;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import tko.database.entity.workout.ExerciseEntity;
import tko.database.entity.workout.LikesExerciseEntity;
import tko.database.repository.user.UsersRepository;
import tko.database.repository.workout.ExerciseRepository;
import tko.database.repository.workout.LikesExerciseRepository;
import tko.database.repository.workout.WorkoutExerciseRepository;
import tko.database.specification.ExerciseSpecifications;
import tko.model.dto.workout.*;
import tko.model.mapper.workout.ExerciseMapper;
import tko.utils.DifficultyLevel;
import tko.utils.ExerciseSort;
import tko.utils.ExerciseType;
import tko.utils.personSVG.Muscle;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Сервис управления упражнениями.
 * <p>
 * Методы:
 * <ul>
 *     <li>{@link #create(ExerciseDTO)} — создание нового упражнения</li>
 *     <li>{@link #read(Long)} — получение упражнения по идентификатору</li>
 *     <li>{@link #update(Long, ExerciseDTO)} — обновление информации об упражнении</li>
 *     <li>{@link #delete(Long)} — удаление упражнения (если оно не используется в тренировках)</li>
 *     <li>{@link #readPageable(Pageable)} — получение списка упражнений с постраничной навигацией</li>
 *     <li>{@link #readCatalog(ExerciseCatalogFilter, int, int, ExerciseSort, Sort.Direction)} — каталог с фильтрами и сортировкой</li>
 *     <li>{@link #getFilterOptions()} — доступные значения фильтров</li>
 *     <li>{@link #addLike(Long)} — добавление лайка на упражнение</li>
 *     <li>{@link #removeLike(Long)} — удаление лайка с упражнения</li>
 *     <li>{@link #getMetaData(Long)} — получение медиафайлов (изображения и видео) упражнения</li>
 *     <li>{@link #updateMedia(Long, ExerciseMediaDTO)} — обновление медиафайлов упражнения</li>
 *     <li>{@link #getImage(Long)} — получение изображения упражнения</li>
 *     <li>{@link #getVideo(Long)} — получение видео упражнения</li>
 * </ul>
 */

@Service
public class ExerciseService {
    public static final int MAX_PAGE_SIZE = 100;

    private final ExerciseRepository exerciseRepository;
    private final ExerciseMapper exerciseMapper;
    private final WorkoutExerciseRepository workoutExerciseRepository;
    private final LikesExerciseRepository likesExerciseRepository;
    private final UsersRepository usersRepository;

    @Autowired
    public ExerciseService(ExerciseRepository exerciseRepository, ExerciseMapper exerciseMapper, WorkoutExerciseRepository workoutExerciseRepository, LikesExerciseRepository likesExerciseRepository, UsersRepository usersRepository) {
        this.exerciseRepository = exerciseRepository;
        this.exerciseMapper = exerciseMapper;
        this.workoutExerciseRepository = workoutExerciseRepository;
        this.likesExerciseRepository = likesExerciseRepository;
        this.usersRepository = usersRepository;
    }

    public ExerciseDTO create(ExerciseDTO exerciseDTO) {
        if(exerciseDTO.getId() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Id must be null");
        }

        if(exerciseDTO.getLikeCount() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Like count must be null");
        }
        try{
            ExerciseEntity exerciseEntity = exerciseMapper.toEntity(exerciseDTO);
            exerciseEntity.setLikeCount(0);
            exerciseEntity.setUsageCount(0);
            ExerciseEntity saveEntity = exerciseRepository.save(exerciseEntity);
            return exerciseMapper.toDto(saveEntity);
        }catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Invalid exercise data");
        }
    }

    public ExerciseDTO read(Long id) {
        if(id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Id must not be null");
        }
        ExerciseEntity exerciseEntity = exerciseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id not found"));
        return exerciseMapper.toDto(exerciseEntity);
    }

    public ExerciseDTO update(Long id, ExerciseDTO exerciseDTO) {
        if(id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Id must not be null");
        }
        if(exerciseDTO.getLikeCount() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Like count must be null");
        }

        ExerciseEntity exerciseEntity = exerciseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id not found"));
        exerciseMapper.updateEntity(exerciseDTO,exerciseEntity);

        if(exerciseEntity.getMuscularGroup().contains(null)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Invalid muscular group");
        }

        ExerciseEntity saveEntity = exerciseRepository.save(exerciseEntity);
        return exerciseMapper.toDto(saveEntity);
    }

    public ExerciseDTO delete(Long id) {
        if(id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Id must not be null");
        }
        ExerciseEntity exerciseEntity = exerciseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id not found"));

        boolean hasDependencies = workoutExerciseRepository.existsByExercise_Id(id);

        if(hasDependencies) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot delete exercise: it is used in workout");
        }

        List<LikesExerciseEntity> likesExerciseEntities = likesExerciseRepository.findAllByExercise_Id(id);

        likesExerciseRepository.deleteAll(likesExerciseEntities);
        exerciseRepository.delete(exerciseEntity);
        return exerciseMapper.toDto(exerciseEntity);
    }

    /**
     * Старый эндпоинт /exercise/page.
     * Исправлено: раньше возвращался new PageImpl<>(list) — терялись totalElements/totalPages,
     * и пользователь искался в БД на каждой строке.
     */
    @Transactional(readOnly = true)
    public Page<ExerciseForPageDTO> readPageable(Pageable pageable) {
        if(pageable == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pageable must not be null");
        }
        Long userId = currentUserId();
        return exerciseRepository.findAll(pageable).map(e -> toPageDto(e, userId));
    }

    /**
     * Каталог упражнений с фильтрацией и сортировкой.
     *
     * @param filter    фильтры (мышцы, сложность, тип, рейтинг, популярность, ...)
     * @param page      номер страницы с 0
     * @param size      размер страницы (1..100)
     * @param sort      RATING / POPULARITY / NAME / NEWEST
     * @param direction ASC / DESC (null — направление по умолчанию для sort)
     */
    @Transactional(readOnly = true)
    public Page<ExerciseForPageDTO> readCatalog(ExerciseCatalogFilter filter, int page, int size,
                                                ExerciseSort sort, Sort.Direction direction) {
        if (page < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page must be >= 0");
        }
        if (size < 1 || size > MAX_PAGE_SIZE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Size must be between 1 and " + MAX_PAGE_SIZE);
        }
        if (filter != null) {
            if (filter.getMinLikes() != null && filter.getMinLikes() < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "minLikes must be >= 0");
            }
            if (filter.getMinUsage() != null && filter.getMinUsage() < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "minUsage must be >= 0");
            }
        }

        ExerciseSort effectiveSort = sort != null ? sort : ExerciseSort.RATING;
        Pageable pageable = PageRequest.of(page, size, effectiveSort.toSort(direction));
        Long userId = currentUserId();

        Page<ExerciseEntity> result = exerciseRepository.findAll(
                ExerciseSpecifications.byFilter(filter, userId), pageable);

        return result.map(e -> toPageDto(e, userId));
    }

    /** Значения для построения панели фильтров на клиенте. */
    public ExerciseFilterOptionsDTO getFilterOptions() {
        return new ExerciseFilterOptionsDTO(
                Arrays.stream(Muscle.values()).map(Enum::name).toList(),
                Arrays.stream(DifficultyLevel.values()).map(Enum::name).toList(),
                Arrays.stream(ExerciseType.values()).map(Enum::name).toList(),
                Arrays.stream(ExerciseSort.values()).map(Enum::name).toList(),
                Arrays.stream(Sort.Direction.values()).map(Enum::name).toList()
        );
    }

    public void addLike(Long id) {
        if(id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Id must not be null");
        }

        ExerciseEntity exerciseEntity = exerciseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id not found"));
        Integer likeCount = exerciseEntity.getLikeCount();
        likeCount = likeCount + 1;
        exerciseEntity.setLikeCount(likeCount);
        exerciseRepository.save(exerciseEntity);
    }

    public void removeLike(Long id) {
        if(id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Id must not be null");
        }

        ExerciseEntity exerciseEntity = exerciseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id not found"));
        Integer likeCount = exerciseEntity.getLikeCount();
        likeCount = likeCount - 1;
        exerciseEntity.setLikeCount(likeCount);
        exerciseRepository.save(exerciseEntity);
    }

    public ExerciseMediaDTO getMetaData(Long id) {
        if(id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Id must not be null");
        }

        ExerciseEntity entity = exerciseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id not found"));
        ExerciseMediaDTO dto = new ExerciseMediaDTO();

        dto.setId(entity.getId());
        dto.setUrlImage(entity.getPhotoUrl());
        dto.setUrlVideo(entity.getVideoUrl());
        dto.setImageUpdated(entity.getImageUpdated());
        dto.setVideoUpdated(entity.getVideoUpdated());

        return dto;
    }

    public ExerciseDTO updateMedia(Long id, ExerciseMediaDTO exerciseMediaDTO) {
        if(id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Id must not be null");
        }
        ExerciseEntity entity = exerciseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id not found"));

        if(exerciseMediaDTO.getUrlImage() == null && exerciseMediaDTO.getUrlVideo() == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Invalid url of video and image");
        }

        if(exerciseMediaDTO.getUrlImage() != null) {
            entity.setPhotoUrl(exerciseMediaDTO.getUrlImage());
            entity.setImageUpdated(LocalDateTime.now());
        }
        if(exerciseMediaDTO.getUrlVideo() != null) {
            entity.setVideoUrl(exerciseMediaDTO.getUrlVideo());
            entity.setVideoUpdated(LocalDateTime.now());
        }

        exerciseRepository.save(entity);
        return exerciseMapper.toDto(entity);
    }

    public Resource getImage(Long id) {

        if(id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Id must not be null");
        }

        ExerciseEntity entity = exerciseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id not found"));
        try {
            if(entity.getPhotoUrl() == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "VideoUrl must not be null");
            }

            String fileName = "/app/media/image/" + entity.getPhotoUrl();

            Path file = Paths.get(fileName);
            Resource resource = new FileSystemResource(file);

            if(resource.exists() && resource.isReadable()) {
                return resource;
            }
            else{
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found or not readable");
            }
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    public Resource getVideo(Long id) {
        if(id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Id must not be null");
        }

        ExerciseEntity entity = exerciseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id not found"));

        try {
            if(entity.getVideoUrl() == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "VideoUrl must not be null");
            }

            String fileName = "/app/media/video/" + entity.getVideoUrl();


            Path file = Paths.get(fileName);
            Resource resource = new FileSystemResource(file);

            if(resource.exists() && resource.isReadable()) {
                return resource;
            }
            else{
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found or not readable");
            }
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    // ---------------------------------------------------------------------

    private ExerciseForPageDTO toPageDto(ExerciseEntity e, Long userId) {
        ExerciseForPageDTO dto = new ExerciseForPageDTO();
        dto.setId(e.getId());
        dto.setName(e.getName());
        dto.setType(e.getType());
        dto.setDifficult(e.getDifficult());
        dto.setRequiresEquipment(e.getRequiresEquipment());
        dto.setMuscularGroup(e.getMuscularGroup());
        dto.setLikeCount(e.getLikeCount() != null ? e.getLikeCount() : 0);
        dto.setUsageCount(e.getUsageCount() != null ? e.getUsageCount() : 0);
        dto.setHasImage(e.getPhotoUrl() != null);
        dto.setLiked(userId != null && likesExerciseRepository.existsByUser_IdAndExercise_Id(userId, e.getId()));
        return dto;
    }

    /** id текущего пользователя (один запрос на страницу, а не на каждую строку). */
    private Long currentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            return null;
        }
        var user = usersRepository.findByLogin(auth.getName());
        return user != null ? user.getId() : null;
    }
}
