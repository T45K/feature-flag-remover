package io.github.t45k.feature_flag_remover.internal

import kotlinx.ast.common.ast.Ast
import kotlinx.ast.common.ast.AstNode

data class ValueArgumentAstNode(
    private val ast: AstNode,
    override val sourceRange: IntRange,
) : RemoveCandidateAstNode {
    companion object {
        fun fromAst(ast: Ast): ValueArgumentAstNode? =
            if (ast.description == "valueArgument") {
                ValueArgumentAstNode(ast as AstNode, ast.getSourceRange())
            } else {
                null
            }
    }

    override fun isRemoveTarget(targetName: String): Boolean =
        ast.children.any { RemoveAfterReleaseAnnotationAstNode.fromAst(it)?.targetName == targetName }
}
