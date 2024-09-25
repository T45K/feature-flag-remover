package io.github.t45k.feature_flag_remover.internal.ast.removeTarget

import io.github.t45k.feature_flag_remover.internal.get
import io.github.t45k.feature_flag_remover.internal.getSourceRange
import kotlinx.ast.common.ast.Ast
import kotlinx.ast.common.ast.AstNode

class ClassAstNode(
    private val ast: AstNode,
    override val sourceRange: IntRange,
) : RemoveCandidateAstNode {
    companion object {
        fun fromAst(ast: Ast): ClassAstNode? =
            if (ast.description == "classDeclaration") {
                ClassAstNode(ast as AstNode, ast.getSourceRange())
            } else {
                null
            }
    }

    override fun isRemoveTarget(targetName: String): Boolean =
        (ast["modifiers"] as? AstNode)
            ?.children
            ?.any { RemoveAfterReleaseAnnotationAstNode.fromAst(it)?.targetName == targetName } == true
}
