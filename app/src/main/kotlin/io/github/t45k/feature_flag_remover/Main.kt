package io.github.t45k.feature_flag_remover

import kotlin.io.path.Path
import kotlinx.ast.common.AstSource
import kotlinx.ast.common.ast.Ast
import kotlinx.ast.common.ast.AstNode
import kotlinx.ast.common.ast.AstTerminal
import kotlinx.ast.common.ast.astInfoOrNull
import kotlinx.ast.grammar.kotlin.target.antlr.kotlin.KotlinGrammarAntlrKotlinParser

fun main() {
    val source = AstSource.File(Path("/Users/t45k/prog/feature_flag_remover/app/src/main/kotlin/io/github/t45k/feature_flag_remover/Sample.kt").toAbsolutePath().toString())
    val kotlinFile = KotlinGrammarAntlrKotlinParser.parseKotlinFile(source)
    search(kotlinFile)
}

private fun search(ast: Ast) {
    if (ast is AstNode && ast.description == "statement") {
        handleStatement(ast)
        println(ast.astInfoOrNull)
    }

    if (ast is AstNode) {
        ast.children.forEach { search(it) }
    }
}

private fun handleStatement(ast: AstNode) {
    hasRemoveAfterReleaseAnnotation(ast)
}

private operator fun Ast?.get(childName: String): Ast? =
    when (this) {
        null -> null
        is AstNode -> this.children.firstOrNull { it.description == childName }
        else -> if (description == childName) this else null
    }

private fun Ast.findTerminalByDescription(description: String): AstTerminal? =
    if (this is AstTerminal && this.description == description) this
    else if (this is AstNode) children.firstNotNullOfOrNull { it.findTerminalByDescription(description) }
    else null

private fun Ast.findTerminalByText(text: String): AstTerminal? =
    if (this is AstTerminal && this.text == text) this
    else if (this is AstNode) children.firstNotNullOfOrNull { it.findTerminalByDescription(text) }
    else null

private fun hasRemoveAfterReleaseAnnotation(ast: AstNode): Boolean {
    return if (ast.description == "annotation") {
        println(ast.findTerminalByDescription("Identifier")?.text)
        println(ast.findTerminalByDescription("LineStrText")?.text)
        true
    } else {
        ast.children.filterIsInstance<AstNode>().any { hasRemoveAfterReleaseAnnotation(it) }
    }

}

@Retention(AnnotationRetention.SOURCE)
@Target(
    AnnotationTarget.EXPRESSION,
)
annotation class RemoveAfterRelease(val target: String)
