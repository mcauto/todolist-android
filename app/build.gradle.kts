plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    kotlin("kapt")
    id("io.gitlab.arturbosch.detekt").version("1.9.1")
}

detekt {
    toolVersion = "1.9.1"
    config = files("../config/detekt/detekt.yml")
    buildUponDefaultConfig = true
}
apply(from = "../ktlint.gradle.kts")

android {
    compileSdkVersion(29)
    buildToolsVersion("29.0.3")

    defaultConfig {
        applicationId = "com.deo.todolist"
        minSdkVersion(21)
        targetSdkVersion(29)
        versionCode = 9
        versionName = "1.4.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(project(":repository"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.3.72")
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("androidx.core:core-ktx:1.3.0")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")

    // LiveData
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.0-alpha04")

    // viewModels()
    implementation("androidx.fragment:fragment-ktx:1.2.5")

    // MultiDex
    implementation("androidx.multidex:multidex:2.0.1")

    // Test
    testImplementation("junit:junit:4.13")
    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")

    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.3")

    // Coroutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.5")
}

/** Fix: androidx 생기면서 생긴 호환성 문제
 * Activity.onCreate에서 더러운 로그를 남기는데 없애주자
 * Rejecting re-init on previously-failed class java.lang.Class
 * <androidx.core.view.ViewCompat$2>: java.lang.NoClassDefFoundError:
 * Failed resolution of: Landroid/view/View$OnUnhandledKeyEventListener
 */
configurations.all {
    resolutionStrategy.eachDependency {
        val requested = this.requested
        if (requested.group == "androidx") {
            if (!requested.name.startsWith("multidex")) {
                this.useVersion("1.+")
            }
        }
    }
}