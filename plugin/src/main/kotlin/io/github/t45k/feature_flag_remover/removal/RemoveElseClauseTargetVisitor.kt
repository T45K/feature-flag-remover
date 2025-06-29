package io.github.t45k.feature_flag_remover.removal

import io.github.t45k.feature_flag_remover.api.RemoveElseClauseAfterRelease
import org.jetbrains.kotlin.psi.KtIfExpression
import org.jetbrains.kotlin.psi.KtTreeVisitorVoid
import org.jetbrains.kotlin.psi.psiUtil.getAnnotationEntries

class RemoveElseClauseTargetVisitor(private val targetName: String) : KtTreeVisitorVoid() {
    private val _removeTargetElements: MutableList<KtIfExpression> = mutableListOf()
    val removeTargetElements: List<KtIfExpression> get() = _removeTargetElements

    override fun visitIfExpression(expression: KtIfExpression) {
        val isAnnotatedAsRemoveTarget = expression.getAnnotationEntries().any { entry ->
            entry.shortName?.asString() == RemoveElseClauseAfterRelease::class.simpleName &&
                entry.valueArguments.any { arg -> arg.getArgumentExpression()?.text == "\"$targetName\"" }
        }

        if (isAnnotatedAsRemoveTarget) {
            _removeTargetElements.add(expression)
        } else {
            super.visitIfExpression(expression)
        }
    }
}
