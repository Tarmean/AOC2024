plugins {
    kotlin("jvm") version "2.0.21"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("com.github.h0tk3y.betterParse:better-parse:0.4.4")
    implementation( "org.jetbrains.kotlinx:multik-core:0.2.2")
    implementation( "org.jetbrains.kotlinx:multik-default:0.2.2" )
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(18)
}