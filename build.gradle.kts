import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.10"
}
group = "me.shubhangiwakodikar"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
dependencies {
    testImplementation(kotlin("test-junit5"))
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.4.10")
}
tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}