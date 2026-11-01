package tk.ssau.fitnesstko

import okhttp3.Interceptor
import okhttp3.Response

/**
 * OkHttp-интерцептор для добавления JWT-токена авторизации к HTTP-запросам.
 *
 * Основные функции:
 * - Добавляет заголовок `Authorization: Bearer <токен>` ко всем исходящим запросам.
 * - Логирует URL и заголовки запросов для отладки.
 *
 * Использование:
 * 1. Добавляется в цепочку OkHttp-клиента через `OkHttpClient.Builder().addInterceptor(AuthInterceptor())`.
 * 2. Автоматически применяется ко всем запросам через Retrofit.
 */
class AuthInterceptor(private val authManager: AuthManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        authManager.getToken()?.let { token ->
            request.addHeader("Authorization", "Bearer $token")
        }
        return chain.proceed(request.build())
    }
}