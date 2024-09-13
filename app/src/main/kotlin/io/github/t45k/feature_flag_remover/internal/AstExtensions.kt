package io.github.t45k.feature_flag_remover.internal

import kotlinx.ast.common.ast.Ast
import kotlinx.ast.common.ast.AstNode
import kotlinx.ast.common.ast.AstTerminal

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
