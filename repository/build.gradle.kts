plugins {
    id("java-library")
    id("kotlin")
    id("io.gitlab.arturbosch.detekt").version("1.9.1")
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
    implementation("com.google.code.gson:gson:2.8.5")
    implementation("com.squareup.retrofit2:retrofit:2.8.1")
    implementation("com.squareup.retrofit2:converter-gson:2.7.1")
    // Test
    testImplementation("junit:junit:4.13")

    // Coroutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.2")

}
