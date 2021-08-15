import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.10"
}
group = "me.shubhangiwakodikar"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
dependencies {
    testImplementation(kotlin("test-junit5"))
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.5.10")
    implementation("io.reactivex.rxjava3:rxjava:3.0.10")
    implementation("io.reactivex:rxjava-guava:1.0.3")
    implementation("com.google.code.gson:gson:2.8.6")
    implementation("com.google.guava:guava:30.1-jre")
    implementation("com.amazonaws:aws-java-sdk-ec2:1.11.975")
    implementation("com.amazonaws:aws-java-sdk-s3:1.11.975")
    implementation("com.amazonaws:aws-java-sdk-dynamodb:1.11.975")
    implementation("com.ning:async-http-client:1.9.39")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0")
    implementation("org.codehaus.groovy:groovy-all:3.0.8")
}
tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}