import static ce.defs.FuncsKt.namespace
import ce.defs.DataType
import ce.defs.NotDefined

def ns = namespace("com.goldman")

def modeType = ns.constantsBlock("ModeType")
modeType.addBlockComment("File mode types")
modeType.defaultType(DataType.int32.INSTANCE)
modeType.add("OREAD", 0)
modeType.add("OWRITE", 1)
modeType.add("ORDWR", 2)
modeType.add("OEXEC", 3)
modeType.add("OTRUNC", 0x10)

ns.dataClass("GroovyData").tap {
    field("firstname", DataType.stringNullable.INSTANCE, NotDefined.INSTANCE)
    field("secondname", DataType.string.INSTANCE)
    field("age", DataType.int32.INSTANCE, 35)
}

def styxQid = ns.dataClass("Qid")
styxQid.with {
    addBlockComment("QID structure")
    field("type", DataType.int32.INSTANCE)
    field("version", DataType.int64.INSTANCE)
    field("path", DataType.int64.INSTANCE)
}
styxQid.addstaticfield("EMPTY", new DataType.custom(styxQid, false), styxQid.instance(
        [type: 0, version: 0, path: 0]
))

def qid2 = ns.dataClass("Qid2")
qid2.with {
    field("type", DataType.int32.INSTANCE, 1)
}
qid2.addstaticfield("EMPTY", new DataType.custom(qid2, false), qid2.instance())