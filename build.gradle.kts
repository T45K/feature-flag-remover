plugins {
    kotlin("jvm") version "2.4.0"

    `maven-publish`
}

group = "com.github.t45k"
version = "1.0.2"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}
