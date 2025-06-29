plugins {
    `java-gradle-plugin`
}

dependencies {
    implementation(project(":api"))
    implementation("org.jetbrains.kotlin:kotlin-compiler-embeddable")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin")
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
