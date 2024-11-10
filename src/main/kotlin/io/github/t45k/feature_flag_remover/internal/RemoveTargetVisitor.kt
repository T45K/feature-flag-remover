package io.github.t45k.feature_flag_remover.internal

import io.github.t45k.feature_flag_remover.api.RemoveAfterRelease
import org.jetbrains.kotlin.psi.KtAnnotated
import org.jetbrains.kotlin.psi.KtAnnotatedExpression
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtElement
import org.jetbrains.kotlin.psi.KtParameter
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.psi.KtTreeVisitorVoid

class RemoveTargetVisitor(private val targetName: String) : KtTreeVisitorVoid() {
    private val _removeTargetElements: MutableList<KtElement> = mutableListOf()
    val removeTargetElements: List<KtElement> get() = _removeTargetElements

    override fun visitClassOrObject(classOrObject: KtClassOrObject) {
        if (classOrObject.isAnnotatedAsRemoveTarget()) {
            _removeTargetElements.add(classOrObject)
        } else {
            super.visitClassOrObject(classOrObject)
        }
    }

    override fun visitProperty(property: KtProperty) {
        if (property.isAnnotatedAsRemoveTarget()) {
            _removeTargetElements.add(property)
        } else {
            super.visitProperty(property)
        }
    }

    override fun visitParameter(parameter: KtParameter) {
        if (parameter.isAnnotatedAsRemoveTarget()) {
            _removeTargetElements.add(parameter)
        } else {
            super.visitParameter(parameter)
        }
    }

    override fun visitAnnotatedExpression(expression: KtAnnotatedExpression) {
        if (expression.isAnnotatedAsRemoveTarget()) {
            _removeTargetElements.add(expression)
        } else {
            return super.visitAnnotatedExpression(expression)
        }
    }

    private fun KtAnnotated.isAnnotatedAsRemoveTarget(): Boolean =
        this.annotationEntries.any { entry ->
            entry.shortName?.asString() == RemoveAfterRelease::class.simpleName &&
                entry.valueArguments.any { arg -> arg.getArgumentExpression()?.text == "\"$targetName\"" }
        }
}