package io.github.t45k.gradle.plugin

import io.github.t45k.feature_flag_remover.api.removeFeatureFlagContext
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
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
        val sourceSets = project.extensions.findByName("sourceSets") as? org.gradle.api.tasks.SourceSetContainer
        
        val sourceDirectories = if (sourceSets != null) {
            sourceSets.flatMap { sourceSet ->
                sourceSet.allSource.srcDirs
            }
        } else {
            listOf(File(project.projectDir, "src/main/kotlin"))
        }
        
        sourceDirectories.forEach { srcDir ->
            if (srcDir.exists()) {
                processDirectory(srcDir, feature)
            }
        }
        
        println("Feature flag '$feature' removal completed.")
    }
    
    private fun processDirectory(directory: File, featureName: String) {
        directory.walkTopDown()
            .filter { it.isFile && it.extension == "kt" }
            .forEach { file ->
                processKotlinFile(file, featureName)
            }
    }
    
    private fun processKotlinFile(file: File, featureName: String) {
        val originalContent = file.readText()
        
        removeFeatureFlagContext {
            val processedContent = removeFeatureFlag(originalContent, featureName)
            
            if (originalContent != processedContent) {
                file.writeText(processedContent)
                println("Processed: ${file.relativeTo(project.projectDir)}")
            }
        }
    }
}