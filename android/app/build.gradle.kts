plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
}

android {
    namespace = "tk.ssau.fitnesstko"
    compileSdk = 35

    defaultConfig {
        applicationId = "tk.ssau.fitnesstko"
        minSdk = 34
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.javafaker)
    implementation("com.google.android.material:material:1.12.0")

    implementation("com.jakewharton.threetenabp:threetenabp:1.4.4")

    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    implementation("androidx.gridlayout:gridlayout:1.0.0")

    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation("androidx.security:security-crypto:1.1.0-alpha06")

    implementation ("com.squareup.okhttp3:okhttp:4.10.0")

    implementation ("com.squareup.okhttp3:logging-interceptor:4.10.0")

    implementation("com.caverock:androidsvg:1.4")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")

    implementation("com.github.bumptech.glide:glide:4.16.0")

    implementation ("com.github.bumptech.glide:okhttp3-integration:4.16.0")
    kapt("com.github.bumptech.glide:compiler:4.16.0")

    implementation("androidx.media3:media3-exoplayer:1.3.1")
    implementation("androidx.media3:media3-ui:1.3.1")
    implementation("androidx.media3:media3-common:1.3.1")
    implementation("androidx.media3:media3-datasource-okhttp:1.3.1")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    runtimeOnly(libs.androidx.recyclerview)
}