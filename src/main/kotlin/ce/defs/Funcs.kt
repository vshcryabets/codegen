package ce.defs

import genrators.obj.ClassField
import genrators.obj.ConstantsEnum

var namescpaceDef = StringBuffer()

fun namespace(name : String) {
    namescpaceDef.setLength(0)
    namescpaceDef.append(name)
}

fun enum(name : String ) : ConstantsEnum {
    return ConstantsEnum(name, namescpaceDef.toString())
}
