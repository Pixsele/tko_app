package tk.ssau.fitnesstko.model.dto.user

data class RegisterUsersDTO(
    val name: String,
    val login: String,
    val password: String,
    val birthDay: java.time.LocalDate,
    val weight: Double,
    val height: Double,
    val gender: Gender
)

enum class Gender { MALE, FEMALE }
