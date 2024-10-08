package io.github.t45k.feature_flag_remover.api

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.EXPRESSION)
annotation class RemoveElseClausAfterRelease(val target: String)
