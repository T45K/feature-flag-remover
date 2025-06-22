plugins {
    kotlin("jvm") version "2.0.0"

    `maven-publish`
}

subprojects {
    apply(plugin = "kotlin")

    repositories {
        mavenCentral()
    }

    group = "com.github.t45k"
    version = "0.1.2"

    dependencies {
        testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
        testImplementation(rootProject.libs.junit.jupiter.engine)
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    }

    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(21)
        }
    }

    tasks.named<Test>("test") {
        useJUnitPlatform()
    }
}

repositories {
    mavenCentral()
}

dependencies {
    api(project(":api"))
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}
