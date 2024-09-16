package io.github.t45k.feature_flag_remover.internal

interface AstNode {
    val sourceRange: IntRange

    fun isRemoveTarget(targetName: String): Boolean
}
