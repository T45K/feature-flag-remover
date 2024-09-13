package io.github.t45k.feature_flag_remover

import io.github.t45k.feature_flag_remover.api.RemoveAfterRelease

class Sample {
    fun sample() {
        @RemoveAfterRelease("sample")
        println("hogehoge")
    }
}
