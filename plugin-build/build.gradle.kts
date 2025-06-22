plugins {
    kotlin("jvm") version "2.0.0"
    `java-gradle-plugin`
    `maven-publish`
}

group = "com.github.t45k"
version = "0.1.2"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-compiler-embeddable")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin")
    implementation("com.google.guava:guava:32.1.3-jre")
    
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

gradlePlugin {
    plugins {
        create("featureFlagRemover") {
            id = "io.github.t45k.feature-flag-remover"
            implementationClass = "io.github.t45k.gradle.plugin.FeatureFlagRemoverPlugin"
            displayName = "Feature Flag Remover Plugin"
            description = "A Gradle plugin to remove feature flags from Kotlin code"
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}