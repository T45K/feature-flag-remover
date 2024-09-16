package io.github.t45k.feature_flag_remover

import io.github.t45k.feature_flag_remover.api.RemoveAfterRelease

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
    }
}
