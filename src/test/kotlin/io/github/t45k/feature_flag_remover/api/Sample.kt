package io.github.t45k.feature_flag_remover.api

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

    @RemoveAfterRelease("sample")
    fun sample2() {
    }

    class Sample2 @RemoveAfterRelease("sample") constructor()
}
