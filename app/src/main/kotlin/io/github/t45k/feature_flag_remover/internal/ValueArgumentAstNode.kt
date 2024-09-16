package io.github.t45k.feature_flag_remover.internal

import kotlinx.ast.common.ast.Ast
import kotlinx.ast.common.ast.AstInfo
import kotlinx.ast.common.ast.AstNode
import kotlinx.ast.common.ast.astAttachmentsOrNull

data class ValueArgumentAstNode(
    private val ast: AstNode,
    override val sourceRange: IntRange,
) : RemoveCandidateAstNode {
    companion object {
        fun fromAst(ast: Ast): ValueArgumentAstNode? =
            if (ast.description == "valueArgument") {
                val sourceRange = ast.astAttachmentsOrNull!!.attachments.values.filterIsInstance<AstInfo>().first()
                    .let { (_, start, stop) -> start.index..<stop.index }
                ValueArgumentAstNode(ast as AstNode, sourceRange)
            } else {
                null
            }
    }

    override fun isRemoveTarget(targetName: String): Boolean =
        ast.children.any { RemoveAfterReleaseAnnotationAstNode.fromAst(it)?.targetName == targetName }
}
