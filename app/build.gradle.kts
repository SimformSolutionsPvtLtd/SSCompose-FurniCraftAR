plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.hilt.plugin)
}

android {
    namespace = App.ID
    compileSdk = libs.versions.compile.sdk.version.get().toInt()

    defaultConfig {
        applicationId = App.ID
        minSdk = libs.versions.min.sdk.version.get().toInt()
        targetSdk = libs.versions.compile.sdk.version.get().toInt()
        versionCode = libs.versions.version.code.get().toInt()
        versionName = libs.versions.version.name.get()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        // SketchFab API key
        with(BuildConfigFields.SKETCHFAB_API_KEY) {
            val sketchFabApiKey = getProperty(value, "apikeys.properties")
            forEach { buildType ->
                buildType.buildConfigField(type, title, sketchFabApiKey)
            }
        }
    }

    splits {
        abi {
            // Enables building multiple APKs per ABI.
            isEnable = true

            // By default all ABIs are included, so use reset() to only include specified.
            reset()

            // Specifies a list of ABIs that Gradle should create APKs for.
            include("x86", "x86_64", "armeabi-v7a", "arm64-v8a")

            // Specifies that we want to also generate a universal APK that includes all ABIs.
            isUniversalApk = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(defaultFileTree())

    // Core
    implementation(libs.androidx.core.ktx)

    // UI
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)

    // ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // Hilt
    implementation(libs.hilt)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.dagger.compiler)
    ksp(libs.hilt.compiler)

    // Material3
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material3.icons.extended)

    // Coroutines
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp3)
    implementation(libs.okhttp3.logging.interceptor)

    // Gson
    implementation(libs.gson)

    // Room
    implementation(libs.room.ktx)
    implementation(libs.room.runtime)
    implementation(libs.room.paging)
    ksp(libs.room.compiler)

    // Paging
    implementation(libs.paging.runtime)
    implementation(libs.paging.compose)

    // Timber
    implementation(libs.timber)

    // Kotlin Reflect
    implementation(libs.kotlin.reflect)

    // Shimmer
    implementation(libs.shimmer)

    // Coil
    implementation(libs.coil)

    // ARSceneView
    implementation(libs.arsceneview)

    // Lottie
    implementation(libs.lottie)

    // Palette
    implementation(libs.palette)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
