package io.github.t45k.feature_flag_remover.internal

import io.github.t45k.feature_flag_remover.internal.ast.removeElseClauseTarget.IfExpressionWithRemoveElseClauseAfterReleaseAnnotationAstNode
import io.github.t45k.feature_flag_remover.internal.ast.removeTarget.ClassAstNode
import io.github.t45k.feature_flag_remover.internal.ast.removeTarget.PropertyAstNode
import io.github.t45k.feature_flag_remover.internal.ast.removeTarget.RemoveCandidateAstNode
import io.github.t45k.feature_flag_remover.internal.ast.removeTarget.StatementAstNode
import io.github.t45k.feature_flag_remover.internal.ast.removeTarget.ValueArgumentAstNode
import kotlinx.ast.common.ast.Ast
import kotlinx.ast.common.ast.AstNode

fun traverseRemoveTarget(ast: Ast, targetName: String): List<RemoveCandidateAstNode> {
    val element = listOf(
        ClassAstNode::fromAst,
        PropertyAstNode::fromAst,
        StatementAstNode::fromAst,
        ValueArgumentAstNode::fromAst,
    ).firstNotNullOfOrNull { astConstructor -> astConstructor(ast)?.takeIf { it.isRemoveTarget(targetName) } }

    return when {
        element != null -> listOf(element)
        ast is AstNode -> ast.children.flatMap { traverseRemoveTarget(it, targetName) }
        else -> emptyList()
    }
}

fun traverseRemoveElseClauseTarget(ast: Ast, targetName: String): List<IfExpressionWithRemoveElseClauseAfterReleaseAnnotationAstNode> {
    val element = IfExpressionWithRemoveElseClauseAfterReleaseAnnotationAstNode.fromAst(ast)
    return when {
        element != null -> listOf(element)
        ast is AstNode -> ast.children.flatMap { traverseRemoveElseClauseTarget(it, targetName) }
        else -> emptyList()
    }
}
