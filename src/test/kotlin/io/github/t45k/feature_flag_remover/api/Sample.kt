@file:Suppress(
    "unused",
    "KotlinConstantConditions",
    "UnusedVariable",
    "ConstantConditionIf",
    "ANNOTATION_WILL_BE_APPLIED_ALSO_TO_PROPERTY_OR_FIELD",
    "USELESS_IS_CHECK",
    "REDUNDANT_ELSE_IN_WHEN",
)

package io.github.t45k.feature_flag_remover.api

class Sample(
    @RemoveAfterRelease("sample") // property
    private val sample: String,
    @RemoveAfterRelease("sample") // property with initial value
    private val sample2: String = "",
    @param:RemoveAfterRelease("sample") // property with use-sites
    private val sample3: String = "",
) {
    fun sample() {
        @RemoveAfterRelease("sample") // expression statement
        println("hogehoge")

        @RemoveAfterRelease("sample") // local variable statement,
        val a = "hogehoge"

        listOf(
            @RemoveAfterRelease("sample") // value argument
            true
        )

        listOf(element = @RemoveAfterRelease("sample") true) // named argument

        val sample = Sample(@RemoveAfterRelease("sample") "sample") // constructor argument

        val b = @RemoveElseClauseAfterRelease("sample") if (true) {
            "enabled"
        } else {
            "disabled"
        }

        val c = @RemoveElseClauseAfterRelease("sample") if (true) "enabled" else "disabled"

        // conditions in when expression
        val d = when ("a") {
            @RemoveAfterRelease("sample") "a" -> "a"
            else -> "b"
        }

        val e = when ("a") {
            is String -> @RemoveAfterRelease("sample") "a"
            else -> "b"
        }

        val f = when ("a") {
            is String -> @RemoveAfterRelease("sample") {
                "a"
            }

            else -> "b"
        }

        val g = when ("a") {
            is String -> {
                @RemoveAfterRelease("sample") "a"
            }

            else -> "b"
        }
    }

    @RemoveAfterRelease("sample")
    fun sample2() {
    }

    class Sample2 @RemoveAfterRelease("sample") constructor()
}
