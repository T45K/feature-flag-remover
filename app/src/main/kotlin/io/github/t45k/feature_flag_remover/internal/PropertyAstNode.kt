package io.github.t45k.feature_flag_remover.internal

import kotlinx.ast.common.ast.Ast
import kotlinx.ast.common.ast.AstInfo
import kotlinx.ast.common.ast.AstNode
import kotlinx.ast.common.ast.astAttachmentsOrNull

// TODO: delete tailing comma
class PropertyAstNode(
    private val ast: AstNode,
    override val sourceRange: IntRange,
) : io.github.t45k.feature_flag_remover.internal.AstNode {
    companion object {
        fun fromAst(ast: Ast): PropertyAstNode? =
            if (ast.description == "classParameter") {
                val sourceRange = ast.astAttachmentsOrNull!!.attachments.values.filterIsInstance<AstInfo>().first()
                    .let { (_, start, stop) -> start.index..<stop.index }
                PropertyAstNode(ast as AstNode, sourceRange)
            } else {
                null
            }
    }

    override fun isRemoveTarget(targetName: String): Boolean =
        (ast["modifiers"] as? AstNode)
            ?.children
            ?.any { RemoveAfterReleaseAnnotationAstNode.fromAst(it)?.targetName == targetName } == true
}
