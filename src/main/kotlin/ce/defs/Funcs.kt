package ce.defs

import generators.obj.input.ConstantsBlock
import generators.obj.input.ConstantsEnum

var namescpaceDef = StringBuffer()
var currentTarget : Target = Target.Other
var customBaseFolderPath = ""

fun namespace(name : String) {
    namescpaceDef.setLength(0)
    namescpaceDef.append(name)
}

fun enum(name : String ) : ConstantsEnum {
    return ConstantsEnum(name, namescpaceDef.toString())
}

fun constantsBlock(name : String ) : ConstantsBlock {
    return ConstantsBlock(name, namescpaceDef.toString())
}

fun target() : Target = currentTarget