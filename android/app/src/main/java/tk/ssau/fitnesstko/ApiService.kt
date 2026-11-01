package tk.ssau.fitnesstko

import android.content.Context
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import tk.ssau.fitnesstko.model.dto.ExerciseDto
import tk.ssau.fitnesstko.model.dto.ExerciseFilterOptionsDto
import tk.ssau.fitnesstko.model.dto.ExerciseForPageDto
import tk.ssau.fitnesstko.model.dto.LikesTrainingsProgramDto
import tk.ssau.fitnesstko.model.dto.LikesWorkoutDto
import tk.ssau.fitnesstko.model.dto.PersonSvgDto
import tk.ssau.fitnesstko.model.dto.PlannedWorkoutDto
import tk.ssau.fitnesstko.model.dto.TrainingsProgramDto
import tk.ssau.fitnesstko.model.dto.TrainingsProgramForPageDTO
import tk.ssau.fitnesstko.model.dto.WorkoutDto
import tk.ssau.fitnesstko.model.dto.WorkoutExerciseDto
import tk.ssau.fitnesstko.model.dto.WorkoutForPageDto
import tk.ssau.fitnesstko.model.dto.WorkoutProgramDto
import tk.ssau.fitnesstko.model.dto.nutrition.KcalProductDTO
import tk.ssau.fitnesstko.model.dto.nutrition.KcalTrackerDTO
import tk.ssau.fitnesstko.model.dto.nutrition.ProductDTO
import tk.ssau.fitnesstko.model.dto.nutrition.ProductForPageDTO
import tk.ssau.fitnesstko.model.dto.user.AuthRequest
import tk.ssau.fitnesstko.model.dto.user.RegisterUsersDTO
import tk.ssau.fitnesstko.model.dto.user.UserDTO
import tk.ssau.fitnesstko.model.dto.user.WeightDto
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Объект синглтон для взаимодействия с REST API сервера
 */
object ApiService {
    internal const val BASE_URL = "http://10.0.2.2:8080/"
    private lateinit var authManager: AuthManager

    fun initialize(context: Context) {
        authManager = AuthManager(context)
    }

    val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(authManager))
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    private val gson = GsonBuilder()
        .registerTypeAdapter(LocalDate::class.java, LocalDateSerializer())
        .create()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val workoutService: WorkoutApi by lazy { retrofit.create(WorkoutApi::class.java) }
    val exerciseService: ExerciseApi by lazy { retrofit.create(ExerciseApi::class.java) }
    val workoutExerciseService: WorkoutExerciseApi by lazy { retrofit.create(WorkoutExerciseApi::class.java) }
    val likesService: LikesApi by lazy { retrofit.create(LikesApi::class.java) }
    val authService: AuthApi by lazy { retrofit.create(AuthApi::class.java) }
    val kcalTrackerService: KcalTrackerApi by lazy { retrofit.create(KcalTrackerApi::class.java) }
    val productService: ProductApi by lazy { retrofit.create(ProductApi::class.java) }
    val programService: ProgramApi by lazy { retrofit.create(ProgramApi::class.java) }
    val workoutProgramService: WorkoutProgramApi by lazy { retrofit.create(WorkoutProgramApi::class.java) }
    val weightService: WeightApi by lazy { retrofit.create(WeightApi::class.java) }

    /**
     * Интерфейс для работы с тренировками
     */
    interface WorkoutApi {
        /**
         * GET запрос на "api/workout/page" для пагинации списка тренировок
         * @param page Номер страницы для пагинации
         * @param size Число тренировок на одной странице
         */
        @GET("api/workout/page")
        fun getWorkouts(
            @Query("page") page: Int = 0,
            @Query("size") size: Int = 200
        ): Call<PagedResponse<WorkoutForPageDto>>

        /**
         * GET запрос на "api/workout/{id}" для получения тренировки по id
         * @param id Уникальный идентификатор тренировки
         */
        @GET("api/workout/{id}")
        fun getWorkoutById(@Path("id") id: Long): Call<WorkoutDto>

        /**
         * POST запрос на "api/workout" для создания тренировки
         * @param workoutDto Созданная тренировка
         */
        @POST("api/workout")
        fun createWorkout(@Body workoutDto: WorkoutDto): Call<WorkoutDto>

        /**
         * GET запрос на "api/workout/svg/{id}" для получения svg картинки человека с мышщами
         * @param id Уникальный идентификатор тренировки
         * @param gender Пол человека на картинке(MALE, GIRL)
         */
        @GET("api/workout/svg/{id}")
        fun getWorkoutSvg(
            @Path("id") id: Long,
            @Query("gender") gender: String
        ): Call<PersonSvgDto>

        @GET("api/planned-workout/list-by-dates/{userId}")
        fun getPlannedWorkouts(
            @Path("userId") userId: Long?,
            @Query("from") from: String,
            @Query("to") to: String
        ): Call<List<PlannedWorkoutDto>>

        @POST("api/planned-workout")
        fun createPlannedWorkout(@Body request: PlannedWorkoutDto): Call<PlannedWorkoutDto>
    }

    /**
     * Интерфейс для работы с упражнениями
     */
    interface ExerciseApi {
        /**
         * GET запрос на "api/exercise/page" для пагинации списка упражнений
         * @param page Номер страницы для пагинации
         * @param size Число упражнений на одной странице
         */
        @GET("api/exercise/page")
        fun getExercises(
            @Query("page") page: Int = 0,
            @Query("size") size: Int = 200
        ): Call<PagedResponse<ExerciseForPageDto>>

        /**
         * GET запрос на "api/exercise/catalog" — каталог с фильтрами и сортировкой.
         * Списки разворачиваются Retrofit'ом в muscles=A&muscles=B, null-параметры не отправляются.
         * @param muscles Группы мышц (enum Muscle)
         * @param matchAllMuscles true — упражнение задействует все выбранные мышцы, false — любую
         * @param difficulties Уровни сложности (EASY, MEDIUM, HARD)
         * @param types Типы упражнений
         * @param requiresEquipment Нужен ли инвентарь
         * @param minLikes Минимальный рейтинг
         * @param minUsage Минимальная популярность
         * @param name Поиск по названию
         * @param onlyLiked Только лайкнутые пользователем
         * @param sort RATING, POPULARITY, NAME, NEWEST
         * @param direction ASC / DESC
         */
        @GET("api/exercise/catalog")
        fun getCatalog(
            @Query("muscles") muscles: List<String>? = null,
            @Query("matchAllMuscles") matchAllMuscles: Boolean? = null,
            @Query("difficulties") difficulties: List<String>? = null,
            @Query("types") types: List<String>? = null,
            @Query("requiresEquipment") requiresEquipment: Boolean? = null,
            @Query("minLikes") minLikes: Int? = null,
            @Query("minUsage") minUsage: Int? = null,
            @Query("name") name: String? = null,
            @Query("onlyLiked") onlyLiked: Boolean? = null,
            @Query("sort") sort: String = "RATING",
            @Query("direction") direction: String? = null,
            @Query("page") page: Int = 0,
            @Query("size") size: Int = 20
        ): Call<PagedResponse<ExerciseForPageDto>>

        /**
         * GET запрос на "api/exercise/filters" — значения для панели фильтров
         */
        @GET("api/exercise/filters")
        fun getFilterOptions(): Call<ExerciseFilterOptionsDto>

        /**
         * GET запрос на "api/exercise/{id}" для детализации упражнения
         * @param id Идентификатор упражнения
         */
        @GET("api/exercise/{id}")
        fun getExerciseDetails(@Path("id") id: Long): Call<ExerciseDto>
    }

    /**
     * Интерфейс для связи тренировок и упражнений
     */
    interface WorkoutExerciseApi {
        /**
         * GET запрос на "api/workout-exercise/workout/{id}" для получения списка упражнений в тренировке
         * @param id Идентификатор тренировки
         */
        @GET("api/workout-exercise/workout/{id}")
        fun getWorkoutExercises(@Path("id") id: Long): Call<List<WorkoutExerciseDto>>

        /**
         * POST запрос на "api/workout-exercise" для создания детальной информации по тренировке
         * @param dto Детализация тренировки
         */
        @POST("api/workout-exercise")
        fun createWorkoutExercise(@Body dto: WorkoutExerciseDto): Call<Unit>
    }

    /**
     * Интерфейс для управления лайками
     */
    interface LikesApi {
        /**
         * POST запрос на "api/like-workout", чтобы поставить лайк тренировке
         * @param like Информация о юзере и тренировке
         */
        @POST("api/like-workout")
        fun likeWorkout(@Body like: LikesWorkoutDto): Call<Unit>

        /**
         * DELETE запрос на "api/like-workout"
         * @param like Информация о юзере и тренировке
         */
        @HTTP(method = "DELETE", path = "api/like-workout", hasBody = true)
        fun deleteLikeWorkout(@Body like: LikesWorkoutDto): Call<Unit>

        @POST("api/like-program")
        fun likeProgram(@Body like: LikesTrainingsProgramDto): Call<Unit>

        @HTTP(method = "DELETE", path = "api/like-program", hasBody = true)
        fun deleteLikeProgram(@Body like: LikesTrainingsProgramDto): Call<Unit>
    }

    interface AuthApi {
        @POST("api/register")
        fun register(@Body user: RegisterUsersDTO): Call<Unit>

        @POST("api/login")
        fun login(@Body authRequest: AuthRequest): Call<AuthResponse>

        @PUT("api/user/{id}")
        fun updateUser(
            @Path("id") id: Long,
            @Body userData: UpdateUserRequest
        ): Call<UserDTO>

        @GET("api/user/{id}")
        fun getUser(@Path("id") id: Long): Call<UserDTO>
    }

    interface KcalTrackerApi {
        @GET("api/kcal-tracker/by-date/{date}")
        fun getKcalByDate(@Path("date") date: String): Call<KcalTrackerDTO>

        @POST("api/kcal-tracker")
        fun createKcalTracker(@Body request: KcalTrackerDTO): Call<KcalTrackerDTO>

        @GET("api/kcal-product/by-tracker/{trackerId}")
        fun getProductsByTracker(@Path("trackerId") trackerId: Long?): Call<List<KcalProductDTO>>

        @POST("api/kcal-product")
        fun createKcalProduct(@Body request: KcalProductDTO): Call<KcalProductDTO>
    }

    interface ProductApi {
        @GET("api/product/{id}")
        fun getProductDetails(@Path("id") id: Long): Call<ProductDTO>

        @GET("api/product/search")
        fun searchProducts(@Query("keyword") keyword: String): Call<List<ProductForPageDTO>>

        @POST("api/product")
        fun createProduct(@Body product: ProductDTO): Call<ProductDTO>
    }

    interface ProgramApi {
        @GET("api/program/page")
        fun getPrograms(
            @Query("page") page: Int = 0,
            @Query("size") size: Int = 200
        ): Call<PagedResponse<TrainingsProgramForPageDTO>>

        @GET("api/program/{id}")
        fun getProgramById(@Path("id") id: Long): Call<TrainingsProgramDto>
    }

    interface WorkoutProgramApi {
        @GET("api/workout-program/workout/{id}")
        fun getWorkoutsByProgram(@Path("id") programId: Long): Call<List<WorkoutProgramDto>>
    }

    interface WeightApi {
        @POST("/api/weight")
        fun postWeight(@Body weight: WeightDto): Call<WeightDto>

        @GET("/api/weight/last/{userId}")
        fun getUserWeights(@Path("userId") userId: Long): Call<List<WeightDto>>
    }


}

/**
 * @param content Список элементов на текущей странице
 * @param page Метаданные пагинации
 */
data class PagedResponse<T>(
    val content: List<T>,
    val page: PageInfo,
)

/**
 * @param size Количество элементов на странице
 * @param number Номер текущей страницы
 * @param totalPages Общее количество страниц
 * @param totalElements Общее количество элементов
 */
data class PageInfo(
    val size: Int,
    val number: Int,
    val totalElements: Int,
    val totalPages: Int
)

data class AuthResponse(
    val token: String,
    val userId: Long,
    val name: String
)

private class LocalDateSerializer : JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
    override fun serialize(
        src: LocalDate,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): LocalDate {
        return LocalDate.parse(json.asString, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    }
}

data class UpdateUserRequest(
    val name: String,
    val login: String,
    val birthDay: String
)
