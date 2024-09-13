package io.github.t45k.feature_flag_remover.api

@Retention(AnnotationRetention.SOURCE)
@Target(
    AnnotationTarget.EXPRESSION,
)
annotation class RemoveAfterRelease(val target: String)
