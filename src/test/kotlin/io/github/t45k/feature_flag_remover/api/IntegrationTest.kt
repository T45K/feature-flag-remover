package io.github.t45k.feature_flag_remover.api

import kotlin.test.Test
import kotlin.test.assertEquals

class IntegrationTest {
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

                val b = @RemoveElseClauseAfterRelease("sample") if (true) {
                    "enabled"
                } else {
                    "disabled"
                }
                
                val c = @RemoveElseClauseAfterRelease("sample") if (true) "enabled" else "disabled"
            }
        }
    """.trimIndent()

    @Test
    fun test() {
        // expect
        assertEquals(
            """
                class Sample(
                    
                ) {
                    fun sample() {
                        

                        

                        val list = listOf(
                            
                        )

                        val sample = Sample()

                        val b = "enabled"
                        
                        val c = "enabled"
                    }
                }
            """.trimIndent(),
            removeFeatureFlagContext { removeFeatureFlag(kotlinFileContent, "sample") },
        )
    }
}
