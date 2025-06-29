package io.github.t45k.feature_flag_remover.removal

import io.github.t45k.feature_flag_remover.util.createSingleKtFile
import kotlin.test.assertEquals
import org.jetbrains.kotlin.psi.psiUtil.endOffset
import org.jetbrains.kotlin.psi.psiUtil.startOffset
import org.junit.jupiter.api.Test

class RemoveElseClauseTargetVisitorTest {
    @Test
    fun `RemoveElseClauseTargetVisitor finds annotated if expression`() {
        // given
        val visitor = RemoveElseClauseTargetVisitor("sample")
        val content = """
            fun main() {
                val foo = @RemoveElseClauseAfterRelease("sample") if (true) { "enabled" } else { "disabled" }
            }
        """.trimIndent()
        val ktFile = createSingleKtFile(content)

        // when
        ktFile.accept(visitor)

        // then
        assertEquals(1, visitor.removeTargetElements.size)
        assertEquals(
            """@RemoveElseClauseAfterRelease("sample") if (true) { "enabled" } else { "disabled" }""",
            content.substring(visitor.removeTargetElements[0].parent.startOffset, visitor.removeTargetElements[0].parent.endOffset),
        )
    }

    @Test
    fun `RemoveElseClauseTargetVisitor finds annotated if expression without braces`() {
        // given
        val visitor = RemoveElseClauseTargetVisitor("sample")
        val content = """
            fun main() {
                val foo = @RemoveElseClauseAfterRelease("sample") if (true) "enabled" else "disabled"
            }
        """.trimIndent()
        val ktFile = createSingleKtFile(content)

        // when
        ktFile.accept(visitor)

        // then
        assertEquals(1, visitor.removeTargetElements.size)
        assertEquals(
            """@RemoveElseClauseAfterRelease("sample") if (true) "enabled" else "disabled"""",
            content.substring(visitor.removeTargetElements[0].parent.startOffset, visitor.removeTargetElements[0].parent.endOffset),
        )
    }
}
