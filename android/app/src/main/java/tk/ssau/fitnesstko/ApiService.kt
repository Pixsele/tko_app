package tk.ssau.fitnesstko

import android.content.Context
import com.google.gson.GsonBuilder
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
import retrofit2.http.Path
import retrofit2.http.Query
import tk.ssau.fitnesstko.model.dto.ExerciseDto
import tk.ssau.fitnesstko.model.dto.ExerciseForPageDto
import tk.ssau.fitnesstko.model.dto.LikesWorkoutDto
import tk.ssau.fitnesstko.model.dto.PersonSvgDto
import tk.ssau.fitnesstko.model.dto.WorkoutDto
import tk.ssau.fitnesstko.model.dto.WorkoutExerciseDto
import tk.ssau.fitnesstko.model.dto.WorkoutForPageDto
import tk.ssau.fitnesstko.model.dto.user.AuthRequest
import tk.ssau.fitnesstko.model.dto.user.RegisterUsersDTO
import java.lang.reflect.Type
import java.time.LocalDate

/**
 * Объект синглтон для взаимодействия с REST API сервера
 */
object ApiService {
    internal const val BASE_URL = "http://85.236.187.180:8080/"
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
            @Query("size") size: Int = 10
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
            @Query("size") size: Int = 20
        ): Call<PagedResponse<ExerciseForPageDto>>

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
    }

    interface AuthApi {
        @POST("api/register")
        fun register(@Body user: RegisterUsersDTO): Call<Unit>

        @POST("api/login")
        fun login(@Body authRequest: AuthRequest): Call<AuthResponse>
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

private class LocalDateSerializer : JsonSerializer<LocalDate> {
    override fun serialize(
        src: LocalDate,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        return JsonPrimitive(src.toString()) // Формат "yyyy-MM-dd"
    }
}