package io.github.t45k.feature_flag_remover.internal.ast.removeTarget

import io.github.t45k.feature_flag_remover.api.RemoveAfterRelease
import io.github.t45k.feature_flag_remover.internal.findTerminalByDescription
import io.github.t45k.feature_flag_remover.internal.findTerminalByText
import io.github.t45k.feature_flag_remover.internal.get
import io.github.t45k.feature_flag_remover.internal.getSourceRange
import kotlinx.ast.common.ast.Ast

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
            val sourceRange = ast.getSourceRange()
            return RemoveAfterReleaseAnnotationAstNode(ast, targetName, sourceRange)
        }
    }
}
