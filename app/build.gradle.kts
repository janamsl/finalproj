plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.finalproj"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.finalproj"
        minSdk = 29
        targetSdk = 34
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    dependencies {
        implementation(platform("com.google.firebase:firebase-bom:33.9.0"))
        implementation(libs.appcompat)
        implementation(libs.material)
        implementation(libs.activity)
        implementation(libs.constraintlayout)
        testImplementation(libs.junit)
        androidTestImplementation(libs.ext.junit)
        androidTestImplementation(libs.espresso.core)

        implementation("com.google.firebase:firebase-analytics")
        implementation("com.google.firebase:firebase-auth") // Firebase Auth
        implementation("com.google.firebase:firebase-database") // Firebase Realtime Database
        implementation("com.google.firebase:firebase-storage") // Add Firebase Storage
    }

}