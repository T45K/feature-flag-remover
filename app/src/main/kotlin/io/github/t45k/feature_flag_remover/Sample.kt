package io.github.t45k.feature_flag_remover

import io.github.t45k.feature_flag_remover.api.RemoveAfterRelease

class Sample(
    @RemoveAfterRelease("sample")
    private val sample: String,
) {
    fun sample() {
        @RemoveAfterRelease("sample")
        println("hogehoge")
    }
}
