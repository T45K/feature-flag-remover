package io.github.t45k.feature_flag_remover.internal

import io.github.t45k.feature_flag_remover.api.RemoveElseClausAfterRelease
import kotlinx.ast.common.ast.Ast

data class IfExpressionWithRemoveElseClauseAfterReleaseAnnotationAstNode private constructor(
    private val ast: Ast,
    val targetName: String,
    val wholeExpressionSourceRange: IntRange,
    val thenClauseSourceRange: IntRange,
) {
    companion object {
        /**
         * @param ast AST node of expression
         */
        fun fromAst(ast: Ast): IfExpressionWithRemoveElseClauseAfterReleaseAnnotationAstNode? {
            if (ast.description != "expression") return null

            val annotation = ast.findNodeByDescription("annotation")
            val targetName = annotation?.findTargetNameRemoveElseClausAfterReleaseAnnotation() ?: return null
            val ifExpression = ast.findNodeByDescription("ifExpression") ?: return null
            val isAnnotationDefinedBeforeIfExpression = annotation.getSourceRange().last < ifExpression.getSourceRange().first
            if (!isAnnotationDefinedBeforeIfExpression) return null

            val thenClauseStatements = ifExpression["controlStructureBody"]["block"]["statements"] ?: return null

            return IfExpressionWithRemoveElseClauseAfterReleaseAnnotationAstNode(
                ast,
                targetName,
                ast.getSourceRange(),
                thenClauseStatements.getSourceRange(),
            )
        }

        private fun Ast.findTargetNameRemoveElseClausAfterReleaseAnnotation(): String? {
            if (description != "annotation") return null

            val annotationInvocationAst = this["singleAnnotation"]["unescapedAnnotation"]["constructorInvocation"]
            if (annotationInvocationAst["userType"]?.findTerminalByText("Identifier")?.text != RemoveElseClausAfterRelease::class.simpleName) return null

            return annotationInvocationAst["valueArguments"]["valueArgument"]["expression"]?.findTerminalByDescription("LineStrText")?.text
        }
    }
}
