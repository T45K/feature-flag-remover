package io.github.t45k.feature_flag_remover.util

import org.jetbrains.kotlin.cli.jvm.compiler.EnvironmentConfigFiles
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.com.intellij.openapi.util.Disposer
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtPsiFactory

fun createSingleKtFile(content: String): KtFile {
    val disposable = Disposer.newDisposable()
    val environment = KotlinCoreEnvironment.createForProduction(
        disposable,
        CompilerConfiguration(),
        EnvironmentConfigFiles.JVM_CONFIG_FILES
    )

    val project = environment.project
    val psiFactory = KtPsiFactory(project)

    return psiFactory.createFile("main.kt", content)
}
