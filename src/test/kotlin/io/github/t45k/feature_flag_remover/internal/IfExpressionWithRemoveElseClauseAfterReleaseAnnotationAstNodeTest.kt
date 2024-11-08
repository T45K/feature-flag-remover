package io.github.t45k.feature_flag_remover.internal

import io.github.t45k.feature_flag_remover.internal.ast.removeElseClauseTarget.IfExpressionWithRemoveElseClauseAfterReleaseAnnotationAstNode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlinx.ast.common.AstSource
import kotlinx.ast.common.ast.AstNode
import kotlinx.ast.grammar.kotlin.target.antlr.kotlin.KotlinGrammarAntlrKotlinParser

class IfExpressionWithRemoveElseClauseAfterReleaseAnnotationAstNodeTest {
    private val sourceCode = """
        val b = @RemoveElseClausAfterRelease("sample") if (true) {
            "enabled"
        } else {
            "disabled"
        }
    """.trimIndent()

    @Test
    fun `fromAst returns not null when giving applicable AST node`() {
        // given
        val source = AstSource.String(description = "dummy", content = sourceCode)
        val expression = KotlinGrammarAntlrKotlinParser.parseKotlinFile(source).findNodeByDescription("expression")!!

        // when
        val ifExpressionWithAnnotation = IfExpressionWithRemoveElseClauseAfterReleaseAnnotationAstNode.fromAst(expression)

        // then
        assertNotNull(ifExpressionWithAnnotation)
        assertEquals("sample", ifExpressionWithAnnotation.targetName)
        assertEquals(
            """
                 @RemoveElseClausAfterRelease("sample") if (true) {
                    "enabled"
                } else {
                    "disabled"
                }
            """.trimIndent(), sourceCode.substring(ifExpressionWithAnnotation.wholeExpressionSourceRange)
        )
        assertEquals(
            "\"enabled\"\n",
            sourceCode.substring(ifExpressionWithAnnotation.thenClauseSourceRange),
        )
    }

    @Test
    fun `fromAst returns null when giving inapplicable AST node`() {
        // given
        val source = AstSource.String(description = "dummy", content = sourceCode)
        val astRoot = KotlinGrammarAntlrKotlinParser.parseKotlinFile(source)

        // when
        val ifExpressionWithAnnotation = IfExpressionWithRemoveElseClauseAfterReleaseAnnotationAstNode.fromAst(astRoot)

        // then
        assertNull(ifExpressionWithAnnotation)
    }

    @Test
    fun `fromAst returns correct node when giving nested if statement`() {
        // given
        val nestedIfStatement = """
            val b = if (true) {
                @RemoveElseClausAfterRelease("sample") if (true) {
                    "enabled"
                } else {
                    "disabled"
                }
            } else {
                "disabled"
            }
        """.trimIndent()

        val source = AstSource.String(description = "dummy", content = nestedIfStatement)
        val firstExpression = KotlinGrammarAntlrKotlinParser.parseKotlinFile(source).findNodeByDescription("expression")!!

        // when
        val ifExpressionWithAnnotation = IfExpressionWithRemoveElseClauseAfterReleaseAnnotationAstNode.fromAst(firstExpression)

        // then
        assertNull(ifExpressionWithAnnotation)

        // when
        val nestedExpression = firstExpression.children.filterIsInstance<AstNode>()
            .firstNotNullOfOrNull { node -> node.findNodeByCondition { IfExpressionWithRemoveElseClauseAfterReleaseAnnotationAstNode.fromAst(it) != null } }!!
        val ifExpressionWithAnnotation2 = IfExpressionWithRemoveElseClauseAfterReleaseAnnotationAstNode.fromAst(nestedExpression)

        // then
        assertNotNull(ifExpressionWithAnnotation2)
        assertEquals(
            """
                | @RemoveElseClausAfterRelease("sample") if (true) {
                |        "enabled"
                |    } else {
                |        "disabled"
                |    }
            """.trimMargin(),
            nestedIfStatement.substring(ifExpressionWithAnnotation2.wholeExpressionSourceRange)
        )
        assertEquals(
            """
                "enabled"
                
            """.trimIndent(),
            nestedIfStatement.substring(ifExpressionWithAnnotation2.thenClauseSourceRange)
        )
    }

    @Test
    fun `fromAst returns correct node when giving if statement without braces`() {
        val ifStatementWithoutBraces = """val b = @RemoveElseClausAfterRelease("sample") if (true) "enabled" else "disabled"""".trimIndent()

        val source = AstSource.String(description = "dummy", content = ifStatementWithoutBraces)
        val expression = KotlinGrammarAntlrKotlinParser.parseKotlinFile(source).findNodeByDescription("expression")!!

        // when
        val ifExpressionWithAnnotation = IfExpressionWithRemoveElseClauseAfterReleaseAnnotationAstNode.fromAst(expression)

        // then
        assertNotNull(ifExpressionWithAnnotation)
        assertEquals("sample", ifExpressionWithAnnotation.targetName)
        assertEquals(
            """ @RemoveElseClausAfterRelease("sample") if (true) "enabled" else "disabled"""",
            ifStatementWithoutBraces.substring(ifExpressionWithAnnotation.wholeExpressionSourceRange),
        )
        assertEquals(
            "\"enabled\"",
            ifStatementWithoutBraces.substring(ifExpressionWithAnnotation.thenClauseSourceRange),
        )
    }
}