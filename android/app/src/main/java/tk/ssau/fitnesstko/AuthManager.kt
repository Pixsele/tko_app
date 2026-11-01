package tk.ssau.fitnesstko

import android.content.Context
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey


/**
 * Менеджер для безопасного хранения и управления JWT-токенами аутентификации.
 * Использует зашифрованные SharedPreferences для защиты конфиденциальных данных.
 *
 * Основные функции:
 * - Сохранение токена в зашифрованном хранилище
 * - Получение токена из хранилища
 * - Удаление токена
 */
class AuthManager(context: Context) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "auth_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    /**
     * Сохраняет JWT-токен в зашифрованном хранилище.
     * @param token Токен для сохранения (например, "eyJhbGci...")
     */
    fun saveUserData(token: String, userId: Long) {
        sharedPreferences.edit {
            putString("jwt_token", token)
            putLong("user_id", userId)
            apply()
        }
    }

    fun getUserId(): Long? {
        return sharedPreferences.getLong("user_id", -1L).takeIf { it != -1L }
    }

    /**
     * Получает сохранённый JWT-токен.
     * @return Токен или null, если не найден
     */
    fun getToken(): String? {
        return sharedPreferences.getString("jwt_token", null)
    }

    /**
     * Удаляет JWT-токен из хранилища.
     * Используется при выходе пользователя из системы.
     */
    fun clearToken() {
        sharedPreferences.edit { remove("jwt_token").apply() }
    }

    fun saveCredentials(login: String, password: String) {
        sharedPreferences.edit {
            putString("login", login)
            putString("password", password)
        }
    }

    fun getLogin(): String? {
        return sharedPreferences.getString("login", null)
    }

    fun getPassword(): String? {
        return sharedPreferences.getString("password", null)
    }

    fun clearCredentials() {
        sharedPreferences.edit {
            remove("login")
            remove("password")
        }
    }
}