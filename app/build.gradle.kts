plugins {
    alias(libs.plugins.jvm)
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.kotlinx.ast:grammar-kotlin-parser-antlr-kotlin:v0.1.0")
    implementation("com.github.drieks.antlr-kotlin:antlr-kotlin-runtime-jvm:master")
    implementation("com.soywiz.korlibs.korio:korio-jvm:2.0.10")

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation(libs.junit.jupiter.engine)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation(libs.guava)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
