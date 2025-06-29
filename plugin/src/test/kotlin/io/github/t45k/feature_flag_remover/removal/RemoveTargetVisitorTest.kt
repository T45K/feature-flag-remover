package io.github.t45k.feature_flag_remover.removal

import io.github.t45k.feature_flag_remover.util.createSingleKtFile
import kotlin.test.Test
import kotlin.test.assertEquals
import org.jetbrains.kotlin.psi.psiUtil.endOffset
import org.jetbrains.kotlin.psi.psiUtil.startOffset

class RemoveTargetVisitorTest {
    @Test
    fun `RemoveTargetVisitor finds annotated local variable declaration`() {
        // given
        val visitor = RemoveTargetVisitor("sample")
        val content = """
            fun main() {
                @RemoveAfterRelease("sample")
                val foo = "bar"
            }
        """.trimIndent()
        val ktFile = createSingleKtFile(content)

        // when
        ktFile.accept(visitor)

        // then
        assertEquals(1, visitor.removeTargetElements.size)
        assertEquals(
            """
                @RemoveAfterRelease("sample")
                    val foo = "bar"
            """.trimIndent(),
            content.substring(visitor.removeTargetElements[0].startOffset, visitor.removeTargetElements[0].endOffset),
        )
    }

    @Test
    fun `RemoveTargetVisitor finds annotated class property`() {
        // given
        val visitor = RemoveTargetVisitor("sample")
        val content = """
            class Main(@RemoveAfterRelease("sample") val foo: String)
        """.trimIndent()
        val ktFile = createSingleKtFile(content)

        // when
        ktFile.accept(visitor)

        // then
        assertEquals(1, visitor.removeTargetElements.size)
        assertEquals(
            """@RemoveAfterRelease("sample") val foo: String""",
            content.substring(visitor.removeTargetElements[0].startOffset, visitor.removeTargetElements[0].endOffset),
        )
    }

    @Test
    fun `RemoveTargetVisitor finds annotated if expression`() {
        // given
        val visitor = RemoveTargetVisitor("sample")
        val content = """
            fun main() {
                @RemoveAfterRelease("sample")
                if (true) {
                    return "foo"
                }
            }
        """.trimIndent()
        val ktFile = createSingleKtFile(content)

        // when
        ktFile.accept(visitor)

        // then
        assertEquals(1, visitor.removeTargetElements.size)
        assertEquals(
            """
                @RemoveAfterRelease("sample")
                    if (true) {
                        return "foo"
                    }
            """.trimIndent(),
            content.substring(visitor.removeTargetElements[0].startOffset, visitor.removeTargetElements[0].endOffset),
        )
    }


    @Test
    fun `RemoveTargetVisitor finds annotated class declaration`() {
        // given
        val visitor = RemoveTargetVisitor("sample")
        val content = """
            @RemoveAfterRelease("sample")
            class Main
        """.trimIndent()
        val ktFile = createSingleKtFile(content)

        // when
        ktFile.accept(visitor)

        // then
        assertEquals(1, visitor.removeTargetElements.size)
        assertEquals(
            """
                @RemoveAfterRelease("sample")
                class Main
            """.trimIndent(),
            content.substring(visitor.removeTargetElements[0].startOffset, visitor.removeTargetElements[0].endOffset),
        )
    }

    @Test
    fun `RemoveTargetVisitor finds annotated parameter`() {
        // given
        val visitor = RemoveTargetVisitor("sample")
        val content = """
            fun main() {
                println(@RemoveAfterRelease("sample") true)
            }
        """.trimIndent()
        val ktFile = createSingleKtFile(content)

        // when
        ktFile.accept(visitor)

        // then
        assertEquals(1, visitor.removeTargetElements.size)
        assertEquals(
            """@RemoveAfterRelease("sample") true""",
            content.substring(visitor.removeTargetElements[0].startOffset, visitor.removeTargetElements[0].endOffset),
        )
    }

    @Test
    fun `RemoveTargetVisitor finds annotated argument`() {
        // given
        val visitor = RemoveTargetVisitor("sample")
        val content = """
            fun main(@RemoveAfterRelease("sample") foo: String) {
            }
        """.trimIndent()
        val ktFile = createSingleKtFile(content)

        // when
        ktFile.accept(visitor)

        // then
        assertEquals(1, visitor.removeTargetElements.size)
        assertEquals(
            """@RemoveAfterRelease("sample") foo: String""",
            content.substring(visitor.removeTargetElements[0].startOffset, visitor.removeTargetElements[0].endOffset),
        )
    }

    @Test
    fun `RemoveTargetVisitor finds multiple annotated elements`() {
        // given
        val visitor = RemoveTargetVisitor("sample")
        val content = """
            fun main() {
                @RemoveAfterRelease("sample")
                val foo = "bar"

                @RemoveAfterRelease("sample")
                val bar = "baz"                
            }
        """.trimIndent()
        val ktFile = createSingleKtFile(content)

        // when
        ktFile.accept(visitor)

        // then
        assertEquals(2, visitor.removeTargetElements.size)
        assertEquals(
            """
                @RemoveAfterRelease("sample")
                    val foo = "bar"
            """.trimIndent(),
            content.substring(visitor.removeTargetElements[0].startOffset, visitor.removeTargetElements[0].endOffset),
        )
        assertEquals(
            """
                @RemoveAfterRelease("sample")
                    val bar = "baz"
            """.trimIndent(),
            content.substring(visitor.removeTargetElements[1].startOffset, visitor.removeTargetElements[1].endOffset),
        )
    }
}
