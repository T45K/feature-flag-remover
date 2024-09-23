package io.github.t45k.feature_flag_remover.internal

import kotlinx.ast.common.ast.Ast
import kotlinx.ast.common.ast.AstNode

class PropertyAstNode(
    private val ast: AstNode,
    override val sourceRange: IntRange,
) : RemoveCandidateAstNode {
    companion object {
        fun fromAst(ast: Ast): PropertyAstNode? =
            if (ast.description == "classParameter") {
                PropertyAstNode(ast as AstNode, ast.getSourceRange())
            } else {
                null
            }
    }

    override fun isRemoveTarget(targetName: String): Boolean =
        (ast["modifiers"] as? AstNode)
            ?.children
            ?.any { RemoveAfterReleaseAnnotationAstNode.fromAst(it)?.targetName == targetName } == true
}
