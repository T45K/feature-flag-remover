plugins {
    kotlin("jvm") version "2.0.0"

    `maven-publish`
}

group = "com.github.t45k"
version = "0.1.2"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

publishing {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                from(components["java"])
            }
        }
    }
}
