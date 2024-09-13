package io.github.t45k.feature_flag_remover

import io.github.t45k.feature_flag_remover.internal.StatementAstNode
import kotlinx.ast.common.AstSource
import kotlinx.ast.common.ast.Ast
import kotlinx.ast.common.ast.AstNode
import kotlinx.ast.grammar.kotlin.target.antlr.kotlin.KotlinGrammarAntlrKotlinParser
import kotlin.io.path.Path

fun main() {
    val source = AstSource.File(Path("app/src/main/kotlin/io/github/t45k/feature_flag_remover/Sample.kt").toAbsolutePath().toString())
    val kotlinFile = KotlinGrammarAntlrKotlinParser.parseKotlinFile(source)

    val statements = traverse(ast = kotlinFile, targetName = "sample")
    println(statements)
}

private fun traverse(ast: Ast, targetName: String): List<StatementAstNode> {
    val statement = StatementAstNode.fromAst(ast)
    if (statement?.isRemoveTarget(targetName) == true) {
        return listOf(statement)
    }

    return if (ast is AstNode) {
        ast.children.flatMap {
            traverse(it, targetName)
        }
    } else {
        emptyList()
    }
}
