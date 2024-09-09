package io.github.t45k.feature_flag_remover

class Sample {
    fun sample() {
        @RemoveAfterRelease("sample")
        println("hogehoge")
    }
}
