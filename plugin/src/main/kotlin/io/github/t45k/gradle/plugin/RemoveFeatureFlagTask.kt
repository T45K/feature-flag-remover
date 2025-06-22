package io.github.t45k.gradle.plugin

import io.github.t45k.feature_flag_remover.api.ProjectSetup
import io.github.t45k.feature_flag_remover.api.removeFeatureFlagContext
import org.gradle.api.DefaultTask
import org.gradle.api.logging.LogLevel
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import java.io.File

abstract class RemoveFeatureFlagTask : DefaultTask() {

    @get:Input
    @get:Option(option = "feature", description = "The name of the feature flag to remove")
    abstract val featureName: Property<String>

    init {
        group = "feature-flag-remover"
        description = "Remove feature flags from Kotlin source files"
    }

    @TaskAction
    fun removeFeatureFlag() {
        val feature = featureName.get()
        val sourceSets = project.extensions.findByName("sourceSets") as? SourceSetContainer

        val sourceDirectories = sourceSets?.flatMap { it.allSource.srcDirs }
            ?: listOf(
                File(project.projectDir, "src/main/kotlin"),
                File(project.projectDir, "src/test/kotlin"),
            )

        logger.log(LogLevel.INFO, "Start removing '$feature' feature flag from the following directories: $sourceDirectories")

        removeFeatureFlagContext {
            sourceDirectories.forEach { srcDir ->
                if (srcDir.exists()) {
                    processDirectory(srcDir, feature)
                }
            }
        }

        logger.log(LogLevel.INFO, "'$feature' feature flag removal completed")
    }

    private fun ProjectSetup.processDirectory(directory: File, featureName: String) {
        directory.walkTopDown()
            .filter { it.isFile && it.extension == ".kt" }
            .forEach { processKotlinFile(it, featureName) }
    }

    private fun ProjectSetup.processKotlinFile(file: File, featureName: String) {
        logger.log(LogLevel.INFO, "Start processing file '$file'")

        val originalContent = file.readText()
        val processedContent = removeFeatureFlag(originalContent, featureName)

        if (originalContent != processedContent) {
            file.writeText(processedContent)
            logger.log(LogLevel.INFO, "Finished processing file '$file'")
        } else {
            logger.log(LogLevel.INFO, "Skip processing file '$file' because no feature flag was found")
        }
    }
}
