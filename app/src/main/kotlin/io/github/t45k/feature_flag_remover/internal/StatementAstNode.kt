package io.github.t45k.feature_flag_remover.internal

import kotlinx.ast.common.ast.Ast
import kotlinx.ast.common.ast.AstInfo
import kotlinx.ast.common.ast.AstNode
import kotlinx.ast.common.ast.astAttachmentsOrNull

data class StatementAstNode(
    private val ast: AstNode,
    override val sourceRange: IntRange,
) : io.github.t45k.feature_flag_remover.internal.AstNode {
    companion object {
        fun fromAst(ast: Ast): StatementAstNode? =
            if (ast.description == "statement") {
                val sourceRange = ast.astAttachmentsOrNull!!.attachments.values.filterIsInstance<AstInfo>().first()
                    .let { (_, start, stop) -> start.index..<stop.index }
                StatementAstNode(ast as AstNode, sourceRange)
            } else {
                null
            }
    }

    fun isRemoveTarget(targetName: String): Boolean =
        ast.children.any { RemoveAfterReleaseAnnotationAstNode.fromAst(it)?.targetName == targetName }
}
