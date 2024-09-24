package io.github.t45k.feature_flag_remover.api

import io.github.t45k.feature_flag_remover.internal.removeFromText
import io.github.t45k.feature_flag_remover.internal.traverseRemoveElseClauseTarget
import io.github.t45k.feature_flag_remover.internal.traverseRemoveTarget
import kotlin.io.path.isRegularFile
import kotlin.io.path.readText
import kotlin.io.path.writeText
import kotlinx.ast.common.AstSource
import kotlinx.ast.grammar.kotlin.target.antlr.kotlin.KotlinGrammarAntlrKotlinParser
import java.nio.file.Path

fun removeFeatureFlag(kotlinFile: Path, targetName: String) {
    require(kotlinFile.isRegularFile())

    val content = kotlinFile.readText()
    val removedContent = removeFeatureFlag(content, targetName)
    kotlinFile.writeText(removedContent)
}

fun removeFeatureFlag(kotlinFileContent: String, targetName: String): String {
    val astRoot = AstSource.String(description = "Kotlin file", content = kotlinFileContent)
        .let { KotlinGrammarAntlrKotlinParser.parseKotlinFile(it) }

    val removeTarget = traverseRemoveTarget(astRoot, targetName)
    val removeElseClauseTarget = traverseRemoveElseClauseTarget(astRoot, targetName)

    return removeFromText(kotlinFileContent, removeTarget, removeElseClauseTarget)
}
