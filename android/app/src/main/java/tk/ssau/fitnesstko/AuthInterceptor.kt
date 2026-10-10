package tk.ssau.fitnesstko

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val hardcodedToken =
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGUiOiJST0xFX0FETUlOIiwiaWF0IjoxNzQ1ODc2OTg2LCJleHAiOjE3NDU5NDg5ODZ9.ksWcB8Js9CVjh27qcOCQA1LtI8In7mfUW-ntVGxHn90" //

        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $hardcodedToken")
            .build()

        Log.d("Network", "URL: ${request.url}")
        Log.d("Network", "Headers: ${request.headers}")


        return chain.proceed(request)
    }
}