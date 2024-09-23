package io.github.t45k.feature_flag_remover.internal

import kotlinx.ast.common.ast.Ast
import kotlinx.ast.common.ast.AstNode

data class StatementAstNode(
    private val ast: AstNode,
    override val sourceRange: IntRange,
) : RemoveCandidateAstNode {
    companion object {
        fun fromAst(ast: Ast): StatementAstNode? =
            if (ast.description == "statement") {
                StatementAstNode(ast as AstNode, ast.getSourceRange())
            } else {
                null
            }
    }

    override fun isRemoveTarget(targetName: String): Boolean =
        ast.children.any { RemoveAfterReleaseAnnotationAstNode.fromAst(it)?.targetName == targetName }
}
