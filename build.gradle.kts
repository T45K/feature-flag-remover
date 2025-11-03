plugins {
    kotlin("jvm") version "2.2.21"

    `maven-publish`
}

group = "com.github.t45k"
version = "1.0.2"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}
