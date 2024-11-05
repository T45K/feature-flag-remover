package io.github.t45k.feature_flag_remover.api

import kotlin.test.Test
import kotlin.test.assertEquals

class RemoveFeatureFlagKtTest {
    private val kotlinFileContent = """
        class Sample(
            @RemoveAfterRelease("sample") // property
            private val sample: String,
        ) {
            fun sample() {
                @RemoveAfterRelease("sample") // expression statement
                println("hogehoge")

                @RemoveAfterRelease("sample") // local variable statement,
                val a = "hogehoge"

                val list = listOf(
                    @RemoveAfterRelease("sample") // value argument
                    true
                )

                val sample = Sample(@RemoveAfterRelease("sample") sample)

                val b = @RemoveElseClausAfterRelease("sample") if (true) {
                    "enabled"
                } else {
                    "disabled"
                }
                
                val c = @RemoveElseClausAfterRelease("sample") if (true) "enabled" else "disabled"
            }
        }
    """.trimIndent()

    @Test
    fun test() {
        // when
        val removedContent = removeFeatureFlag(kotlinFileContent, "sample")

        // then
        assertEquals(
            """
                class Sample(
                   
                ) {
                    fun sample() {
                       

                       
                        val list = listOf(
                           
                        )

                        val sample = Sample()

                        val b ="enabled"

                        
                        val c ="enabled"
                    }
                }
            """.trimIndent(),
            removedContent,
        )
    }
}
