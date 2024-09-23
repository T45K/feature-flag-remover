package io.github.t45k.feature_flag_remover

import io.github.t45k.feature_flag_remover.internal.ClassAstNode
import io.github.t45k.feature_flag_remover.internal.IfExpressionWithRemoveElseClauseAfterReleaseAnnotationAstNode
import io.github.t45k.feature_flag_remover.internal.PropertyAstNode
import io.github.t45k.feature_flag_remover.internal.RemoveCandidateAstNode
import io.github.t45k.feature_flag_remover.internal.StatementAstNode
import io.github.t45k.feature_flag_remover.internal.ValueArgumentAstNode
import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlinx.ast.common.AstSource
import kotlinx.ast.common.ast.Ast
import kotlinx.ast.common.ast.AstNode
import kotlinx.ast.grammar.kotlin.target.antlr.kotlin.KotlinGrammarAntlrKotlinParser

fun main() {
    val path = Path("app/src/main/kotlin/io/github/t45k/feature_flag_remover/Sample.kt")
    val source = AstSource.File(path.toAbsolutePath().toString())
    val kotlinFile = KotlinGrammarAntlrKotlinParser.parseKotlinFile(source)

    val nodes = traverse(ast = kotlinFile, targetName = "sample")
    val node2 = traverse2(ast = kotlinFile, targetName = "sample")
    println(path.readText().filterIndexed { index, c ->
        val isIndexInRemoveAfterReleaseTarget = nodes.any { index in it.sourceRange }
        val isIndexOfTailingCommaOfRemoveAfterReleaseTarget = c == ',' && nodes.any { index == (it.sourceRange.last + 1) }
        val isIndexInIfExpressionWithRemoveElseClauseAfterReleaseAnnotationTarget = node2.any { index in it.wholeExpressionSourceRange }
        val isIndexInThenClauseOfIfExpressionWithRemoveElseClauseAfterReleaseAnnotationTarget = node2.any { index in it.thenClauseSourceRange }

        val isRemoveTarget =
            isIndexInRemoveAfterReleaseTarget ||
                isIndexOfTailingCommaOfRemoveAfterReleaseTarget ||
                isIndexInIfExpressionWithRemoveElseClauseAfterReleaseAnnotationTarget && !isIndexInThenClauseOfIfExpressionWithRemoveElseClauseAfterReleaseAnnotationTarget

        !isRemoveTarget
    })
}

private fun traverse(ast: Ast, targetName: String): List<RemoveCandidateAstNode> {
    val element = listOf(
        StatementAstNode::fromAst,
        PropertyAstNode::fromAst,
        ClassAstNode::fromAst,
        ValueArgumentAstNode::fromAst,
    ).firstNotNullOfOrNull { astConstructor -> astConstructor(ast)?.takeIf { it.isRemoveTarget(targetName) } }

    return when {
        element != null -> listOf(element)
        ast is AstNode -> ast.children.flatMap { traverse(it, targetName) }
        else -> emptyList()
    }
}

private fun traverse2(ast: Ast, targetName: String): List<IfExpressionWithRemoveElseClauseAfterReleaseAnnotationAstNode> {
    val element = IfExpressionWithRemoveElseClauseAfterReleaseAnnotationAstNode.fromAst(ast)

    return when {
        element != null -> listOf(element)
        ast is AstNode -> ast.children.flatMap { traverse2(it, targetName) }
        else -> emptyList()
    }
}
