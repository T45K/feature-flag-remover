package io.github.t45k.feature_flag_remover.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class FeatureFlagRemoverPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.tasks.register("removeFeatureFlag", RemoveFeatureFlagTask::class.java)
    }
}
