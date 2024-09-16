package io.github.t45k.feature_flag_remover.internal

import kotlinx.ast.common.ast.Ast
import kotlinx.ast.common.ast.AstInfo
import kotlinx.ast.common.ast.AstNode
import kotlinx.ast.common.ast.astAttachmentsOrNull

class ClassAstNode(
    private val ast: AstNode,
    override val sourceRange: IntRange,
) : RemoveCandidateAstNode {
    companion object {
        fun fromAst(ast: Ast): ClassAstNode? =
            if (ast.description == "classDeclaration") {
                val sourceRange = ast.astAttachmentsOrNull!!.attachments.values.filterIsInstance<AstInfo>().first()
                    .let { (_, start, stop) -> start.index..<stop.index }
                ClassAstNode(ast as AstNode, sourceRange)
            } else {
                null
            }
    }

    override fun isRemoveTarget(targetName: String): Boolean =
        (ast["modifiers"] as? AstNode)
            ?.children
            ?.any { RemoveAfterReleaseAnnotationAstNode.fromAst(it)?.targetName == targetName } == true
}

