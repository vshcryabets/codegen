package ce.defs

import generators.obj.input.*

//val globDefinedBlocks = mutableListOf<Block>()
val globRootNamespace = Namespace("", TreeRoot)
var globCurrentNamespace = globRootNamespace
val namespaceMap = NamespaceMap()
var currentTarget: Target = Target.Other
var customBaseFolderPath = ""
var sourceFile = ""
var outputFile = ""

fun namespace(name: String) : Namespace {
    globCurrentNamespace = globRootNamespace.getNamespace(name)
    return globCurrentNamespace
}

fun putDefaults(block: Block) {
    globCurrentNamespace.subs.add(block)
    block.objectBaseFolder = customBaseFolderPath
    block.sourceFile = sourceFile
    block.outputFile = if (outputFile.isEmpty()) block.name else outputFile
    println("Block ${block.name} = ${block.outputFile}");
}

fun enum(name: String): ConstantsEnum {
    return ConstantsEnum(name, globCurrentNamespace).apply {
        putDefaults(this)
    }
}

fun constantsBlock(name: String): ConstantsBlock {
    return ConstantsBlock(name, globCurrentNamespace).apply {
        putDefaults(this)
    }
}

fun namespaceMap(): NamespaceMap {
    return namespaceMap
}

fun dataClass(name: String): DataClass {
    return DataClass(name, globCurrentNamespace)
        .apply {
            putDefaults(this)
        }
}

fun target(): Target = currentTarget

fun setOutputFileName(name: String) {
    outputFile = name
}

fun setOutputBasePath(name: String) {
    customBaseFolderPath = name
}