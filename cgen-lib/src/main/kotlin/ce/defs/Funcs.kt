package ce.defs

import generators.obj.input.*

val globRootNamespace = NamespaceImpl("", TreeRoot)
var currentTarget: Target = Target.Other
var customBaseFolderPath = ""
var sourceFile = ""
var outputFile = ""

fun rootNamespace() : Namespace = globRootNamespace

fun namespace(name: String) : Namespace = globRootNamespace.getNamespace(name)

fun target(): Target = currentTarget

fun setOutputFileName(name: String) {
    outputFile = name
}

fun setOutputBasePath(name: String) {
    customBaseFolderPath = name
}