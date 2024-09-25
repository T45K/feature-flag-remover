package io.github.t45k.feature_flag_remover.internal

import io.github.t45k.feature_flag_remover.internal.ast.removeElseClauseTarget.IfExpressionWithRemoveElseClauseAfterReleaseAnnotationAstNode
import io.github.t45k.feature_flag_remover.internal.ast.removeTarget.RemoveCandidateAstNode

fun removeFromText(
    text: String,
    removeTargets: List<RemoveCandidateAstNode>,
    removeElseClauseTargets: List<IfExpressionWithRemoveElseClauseAfterReleaseAnnotationAstNode>,
): String = text.filterIndexed { index, c ->
    val isIndexInRemoveTarget = removeTargets.any { index in it.sourceRange }
    val isIndexOfTailingCommaOfRemoveTarget = c == ',' && removeTargets.any { index == (it.sourceRange.last + 1) }
    val isIndexInRemoveElseClauseTarget = removeElseClauseTargets.any { index in it.wholeExpressionSourceRange }
    val isIndexInThenClauseOfRemoveElseClauseTarget = removeElseClauseTargets.any { index in it.thenClauseSourceRange }

    val isRemoveTargetChar =
        isIndexInRemoveTarget ||
            isIndexOfTailingCommaOfRemoveTarget ||
            isIndexInRemoveElseClauseTarget && !isIndexInThenClauseOfRemoveElseClauseTarget

    !isRemoveTargetChar
}
