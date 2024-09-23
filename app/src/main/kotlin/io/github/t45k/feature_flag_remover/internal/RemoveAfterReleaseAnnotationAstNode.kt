package io.github.t45k.feature_flag_remover.internal

import io.github.t45k.feature_flag_remover.api.RemoveAfterRelease
import kotlinx.ast.common.ast.Ast
import kotlinx.ast.common.ast.AstInfo
import kotlinx.ast.common.ast.astAttachmentsOrNull

data class RemoveAfterReleaseAnnotationAstNode private constructor(
    private val ast: Ast,
    val targetName: String,
    val sourceRange: IntRange,
) {
    companion object {
        /**
         * @param ast AST node of annotation
         */
        fun fromAst(ast: Ast): RemoveAfterReleaseAnnotationAstNode? {
            if (ast.description != "annotation") return null

            val annotationInvocationAst = ast["singleAnnotation"]["unescapedAnnotation"]["constructorInvocation"]
            if (annotationInvocationAst["userType"]?.findTerminalByText("Identifier")?.text != RemoveAfterRelease::class.simpleName) return null

            val targetName = annotationInvocationAst["valueArguments"]["valueArgument"]["expression"]?.findTerminalByDescription("LineStrText")?.text
                ?: return null
            val sourceRange = ast.astAttachmentsOrNull!!.attachments.values.filterIsInstance<AstInfo>().first()
                .let { (_, start, stop) -> start.index..stop.index }
            return RemoveAfterReleaseAnnotationAstNode(ast, targetName, sourceRange)
        }
    }
}
