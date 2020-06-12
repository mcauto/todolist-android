plugins {
    id("java-library")
    id("kotlin")
    id("io.gitlab.arturbosch.detekt").version("1.9.1")
    kotlin("kapt")
}

detekt {
    toolVersion = "1.9.1"
    config = files("../config/detekt/detekt.yml")
    buildUponDefaultConfig = true
}

apply(from = "../ktlint.gradle.kts")

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.3.72")

    // Test
    testImplementation("junit:junit:4.13")

    // Mockk
    testImplementation("io.mockk:mockk:1.10.0")


    // HTTP Client & Serializer
    val retrofitVersion = "2.9.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")

    // Coroutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.5")

    // Dependency Injection
    val daggerVersion = "2.28"
    kapt("com.google.dagger:dagger-compiler:$daggerVersion")
    kapt("com.google.dagger:dagger-android-processor:$daggerVersion")
    implementation("com.google.dagger:dagger:$daggerVersion")
    implementation("com.google.dagger:dagger-android:$daggerVersion")
    implementation("com.google.dagger:dagger-android-support:$daggerVersion")


}
