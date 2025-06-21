import static ce.defs.FuncsKt.namespace
import ce.defs.DataType
import ce.defs.NotDefined

def ns = namespace("com.goldman")
def enumGoldErrors = ns.enum("GoldErrorsGroovy")
enumGoldErrors.add("OK", 0)
enumGoldErrors.add("BUSY")
enumGoldErrors.add("AUTHERR")
enumGoldErrors.add("PASSLEN")
enumGoldErrors.add("PASSWRONG", 4)

ns.dataClass("GroovyData").tap {
    field("firstname", new DataType.string(true), NotDefined.INSTANCE)
    field("secondname", new DataType.string())
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