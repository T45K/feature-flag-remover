package io.github.t45k.feature_flag_remover.internal

import kotlinx.ast.common.ast.Ast
import kotlinx.ast.common.ast.AstInfo
import kotlinx.ast.common.ast.AstNode
import kotlinx.ast.common.ast.AstTerminal
import kotlinx.ast.common.ast.astAttachmentsOrNull

operator fun Ast?.get(childName: String): Ast? =
    when (this) {
        null -> null
        is AstNode -> this.children.firstOrNull { it.description == childName }
        else -> if (description == childName) this else null
    }

fun Ast.findTerminalByDescription(description: String): AstTerminal? =
    if (this is AstTerminal && this.description == description) this
    else if (this is AstNode) children.firstNotNullOfOrNull { it.findTerminalByDescription(description) }
    else null

fun Ast.findTerminalByText(text: String): AstTerminal? =
    if (this is AstTerminal && this.text == text) this
    else if (this is AstNode) children.firstNotNullOfOrNull { it.findTerminalByDescription(text) }
    else null

fun Ast.findNodeByDescription(description: String): AstNode? =
    if (this is AstNode && this.description == description) this
    else if (this is AstNode) children.firstNotNullOfOrNull { it.findNodeByDescription(description) }
    else null

fun Ast.findNodeByCondition(condition: (AstNode) -> Boolean): AstNode? =
    if (this is AstNode && condition(this)) this
    else if (this is AstNode) children.firstNotNullOfOrNull { it.findNodeByCondition(condition) }
    else null

fun Ast.getSourceRange(): IntRange = astAttachmentsOrNull!!.attachments.values
    .filterIsInstance<AstInfo>()
    .first()
    .let { (_, start, stop) -> start.index..<stop.index }
