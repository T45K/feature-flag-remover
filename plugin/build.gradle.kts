plugins {
    id("com.gradle.plugin-publish") version "1.3.1"
}

dependencies {
    implementation(project(":api"))
    implementation("org.jetbrains.kotlin:kotlin-compiler-embeddable")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin")
}

gradlePlugin {
    website = "https://t45k.github.io"
    vcsUrl = "https://github.com/t45k/feature-flag-remover"
    plugins {
        create("featureFlagRemover") {
            id = "io.github.t45k.feature_flag_remover"
            implementationClass = "io.github.t45k.feature_flag_remover.plugin.FeatureFlagRemoverPlugin"
            displayName = "Feature Flag Remover Plugin"
            description = "A Gradle plugin to remove feature flags from Kotlin code"
            tags = listOf("kotlin", "feature", "flag", "remover")
        }
    }
}
