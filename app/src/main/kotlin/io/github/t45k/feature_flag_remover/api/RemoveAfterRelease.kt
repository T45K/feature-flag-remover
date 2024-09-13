package io.github.t45k.feature_flag_remover.api

@Retention(AnnotationRetention.SOURCE)
@Target(
    AnnotationTarget.EXPRESSION,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.CLASS
)
annotation class RemoveAfterRelease(val target: String)
