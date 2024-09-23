package io.github.t45k.feature_flag_remover.internal

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlinx.ast.common.AstSource
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
    fun testFromAst() {
        // given
        val source = AstSource.String(description = "dummy", content = sourceCode)
        val expression = KotlinGrammarAntlrKotlinParser.parseKotlinFile(source).findNodeByDescription("expression")!!

        // when
        val ifExpressionWithAnnotation = IfExpressionWithRemoveElseClauseAfterReleaseAnnotationAstNode.fromAst(expression)

        // then
        assertNotNull(ifExpressionWithAnnotation)
        assertEquals("sample", ifExpressionWithAnnotation.targetName)
        assertEquals(7..97, ifExpressionWithAnnotation.wholeExpressionSourceRange)
        assertEquals(63..72, ifExpressionWithAnnotation.thenClauseSourceRange)
        assertEquals("\"enabled\"", sourceCode.substring(63..<72))
    }
}