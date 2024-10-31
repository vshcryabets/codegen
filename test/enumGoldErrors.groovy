import static ce.defs.FuncsKt.namespace

def ns = namespace("com.goldman")
def enumGoldErrors = ns.enum("GoldErrorsGroovy")
enumGoldErrors.add("OK", 0)
enumGoldErrors.add("BUSY")
enumGoldErrors.add("AUTHERR")
enumGoldErrors.add("PASSLEN")
enumGoldErrors.add("PASSWRONG", 4)
