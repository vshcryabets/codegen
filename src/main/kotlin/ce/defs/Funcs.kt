package ce.defs

import generators.obj.input.Block
import generators.obj.input.ConstantsBlock
import generators.obj.input.ConstantsEnum

var namescpaceDef = StringBuffer()
var currentTarget : Target = Target.Other
var customBaseFolderPath = ""
var sourceFile = ""
var outputFile = ""

fun namespace(name : String) {
    namescpaceDef.setLength(0)
    namescpaceDef.append(name)
}

fun putDefaults(block: Block) {
    block.objectBaseFolder = customBaseFolderPath
    block.sourceFile = sourceFile
    block.outputFile = if (outputFile.isEmpty()) block.name else outputFile
}


fun enum(name : String ) : ConstantsEnum {
    return ConstantsEnum(name, namescpaceDef.toString()).apply {
        putDefaults(this)

    }
}

fun constantsBlock(name : String ) : ConstantsBlock {
    return ConstantsBlock(name, namescpaceDef.toString()).apply {
        putDefaults(this)
    }
}

fun target() : Target = currentTarget

fun setOutputFileName(name: String) {
    outputFile = name
}