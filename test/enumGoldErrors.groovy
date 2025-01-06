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