import org.gradle.api.tasks.Exec

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

android {
    namespace = "com.andre.githubactionssandbox"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.andre.githubactionssandbox"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

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
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

val printConfigProperty by tasks.registering(Exec::class) {
    group = "custom"
    description = "Prints a property from android.defaultConfig"

    // Get the property name from the project properties
    val configKey: String? by project.extra

    // Set the command to invoke the Kotlin script
    commandLine("kotlin", "-e", """
        val configKey: String? = project.findProperty("configKey") as? String
        val androidExtension = project.extensions.findByName("android")
        val defaultConfig = androidExtension?.javaClass?.getMethod("getDefaultConfig")?.invoke(androidExtension)

        if (propertyName != null) {
            val propertyValue = defaultConfig?.javaClass?.getMethod("getProperty", String::class.java)?.invoke(defaultConfig, propertyName)
            if (propertyValue != null) {
                println("$configKey")
            } else {
                println("Property '$configKey' not found in defaultConfig")
            }
        } else {
            println("Please specify a property name using '-PpropertyName=<property_name>'")
        }
    """.trimIndent())
}