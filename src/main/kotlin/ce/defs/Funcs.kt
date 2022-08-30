package ce.defs

import generators.obj.ConstantsEnum

var namescpaceDef = StringBuffer()

fun namespace(name : String) {
    namescpaceDef.setLength(0)
    namescpaceDef.append(name)
}

fun enum(name : String ) : ConstantsEnum {
    return ConstantsEnum(name, namescpaceDef.toString())
}
