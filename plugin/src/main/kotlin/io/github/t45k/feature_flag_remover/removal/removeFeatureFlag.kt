package io.github.t45k.feature_flag_remover.removal

import org.jetbrains.kotlin.cli.jvm.compiler.EnvironmentConfigFiles
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.com.intellij.openapi.util.Disposer
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.psi.KtPsiFactory

fun removeFeatureFlagContext(block: ProjectSetup.() -> Unit) {
    return ProjectSetup().use {
        it.block()
    }
}

class ProjectSetup : AutoCloseable {
    private val disposable = Disposer.newDisposable()
    private val environment = KotlinCoreEnvironment.createForProduction(
        disposable,
        CompilerConfiguration(),
        EnvironmentConfigFiles.JVM_CONFIG_FILES
    )
    private val project = environment.project
    private val psiFactory = KtPsiFactory(project)

    fun removeFeatureFlag(kotlinFileContent: String, targetName: String): String {
        val ktFile = psiFactory.createFile(kotlinFileContent)
        val removeTargetVisitor = RemoveTargetVisitor(targetName)
        val removeElseClauseTargetVisitor = RemoveElseClauseTargetVisitor(targetName)

        ktFile.accept(removeTargetVisitor)
        ktFile.accept(removeElseClauseTargetVisitor)

        return removeFromText(
            kotlinFileContent,
            removeTargetVisitor.removeTargetElements,
            removeElseClauseTargetVisitor.removeTargetElements,
        )
    }

    override fun close() {
        disposable.dispose()
    }
}
