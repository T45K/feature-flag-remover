package io.github.t45k.feature_flag_remover.removal

import org.jetbrains.kotlin.psi.KtBlockExpression
import org.jetbrains.kotlin.psi.KtElement
import org.jetbrains.kotlin.psi.KtIfExpression
import org.jetbrains.kotlin.psi.psiUtil.endOffset
import org.jetbrains.kotlin.psi.psiUtil.startOffset

internal fun removeFromText(
    text: String,
    removeTargets: List<KtElement>,
    removeElseClauseTargets: List<KtIfExpression>,
): String = text.filterIndexed { index, c ->
    val isIndexInRemoveTarget = removeTargets.any { index in it.startOffset..<it.endOffset }
    val isIndexOfTailingCommaOfRemoveTarget = c == ',' && removeTargets.any { index == it.endOffset }
    val isIndexInRemoveElseClauseTarget = removeElseClauseTargets.any { index in it.parent.startOffset..<it.parent.endOffset }
    val isIndexInThenClauseOfRemoveElseClauseTarget = removeElseClauseTargets.any {
        when (val thenExpression = it.then) {
            is KtBlockExpression ->
                if (thenExpression.statements.isEmpty()) return@any false
                else index in thenExpression.statements.first().startOffset..<thenExpression.statements.last().endOffset

            null -> return@any false
            else -> index in thenExpression.startOffset..<thenExpression.endOffset
        }
    }

    val isRemoveTargetChar =
        isIndexInRemoveTarget ||
            isIndexOfTailingCommaOfRemoveTarget ||
            isIndexInRemoveElseClauseTarget && !isIndexInThenClauseOfRemoveElseClauseTarget

    !isRemoveTargetChar
}
