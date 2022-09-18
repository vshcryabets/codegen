package ce.defs

import generators.obj.input.Block
import generators.obj.input.ConstantsBlock
import generators.obj.input.ConstantsEnum
import generators.obj.input.DataClass

val definedBloks = mutableListOf<Block>()

var namescpaceDef = StringBuffer()
var currentTarget: Target = Target.Other
var customBaseFolderPath = ""
var sourceFile = ""
var outputFile = ""

fun namespace(name: String) {
    namescpaceDef.setLength(0)
    namescpaceDef.append(name)
}

fun putDefaults(block: Block) {
    block.objectBaseFolder = customBaseFolderPath
    block.sourceFile = sourceFile
    block.outputFile = if (outputFile.isEmpty()) block.name else outputFile
    println("Block ${block.name} = ${block.outputFile}");
}

fun enum(name: String): ConstantsEnum {
    return ConstantsEnum(name, namescpaceDef.toString()).apply {
        putDefaults(this)
        definedBloks.add(this)
    }
}

fun constantsBlock(name: String): ConstantsBlock {
    return ConstantsBlock(name, namescpaceDef.toString()).apply {
        putDefaults(this)
        definedBloks.add(this)
    }
}

fun dataClass(name: String): DataClass {
    return DataClass(name, namescpaceDef.toString())
        .apply {
            putDefaults(this)
            definedBloks.add(this)
        }
}

fun target(): Target = currentTarget

fun setOutputFileName(name: String) {
    outputFile = name
}

fun setOutputBasePath(name: String) {
    customBaseFolderPath = name
}