package io.github.t45k.feature_flag_remover

import io.github.t45k.feature_flag_remover.internal.ClassAstNode
import io.github.t45k.feature_flag_remover.internal.PropertyAstNode
import io.github.t45k.feature_flag_remover.internal.StatementAstNode
import kotlinx.ast.common.AstSource
import kotlinx.ast.common.ast.Ast
import kotlinx.ast.common.ast.AstNode
import kotlinx.ast.grammar.kotlin.target.antlr.kotlin.KotlinGrammarAntlrKotlinParser
import kotlin.io.path.Path
import kotlin.io.path.readText

fun main() {
    val path = Path("app/src/main/kotlin/io/github/t45k/feature_flag_remover/Sample.kt")
    val source = AstSource.File(path.toAbsolutePath().toString())
    val kotlinFile = KotlinGrammarAntlrKotlinParser.parseKotlinFile(source)

    val nodes = traverse(ast = kotlinFile, targetName = "sample")
    println(path.readText().filterIndexed { index, c ->
        !(
            nodes.any { index in it.sourceRange } ||
                c == ',' && nodes.any { index == (it.sourceRange.last + 1) })
    })
}

private fun traverse(ast: Ast, targetName: String): List<io.github.t45k.feature_flag_remover.internal.AstNode> {
    val element = listOf(
        StatementAstNode::fromAst,
        PropertyAstNode::fromAst,
        ClassAstNode::fromAst,
    ).firstNotNullOfOrNull { astConstructor -> astConstructor(ast)?.takeIf { it.isRemoveTarget(targetName) } }

    return when {
        element != null -> listOf(element)
        ast is AstNode -> ast.children.flatMap { traverse(it, targetName) }
        else -> emptyList()
    }
}
