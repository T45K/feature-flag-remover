package io.github.t45k.feature_flag_remover.internal.ast.removeTarget

interface RemoveCandidateAstNode {
    val sourceRange: IntRange

    fun isRemoveTarget(targetName: String): Boolean
}
