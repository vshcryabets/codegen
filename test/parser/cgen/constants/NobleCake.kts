import ce.defs.*
import generators.obj.input.*
when (target()) {
    ce.defs.Target.Kotlin -> setOutputBasePath("../kotlin/")
    ce.defs.Target.Cxx -> setOutputBasePath("../cxx/")
    else -> {}
}
namespace("com.goldman.dt1"). apply {
	constantsBlock("NobleCake").apply {
		addBlockComment("NobleCake constants definition block")
		defaultType(DataType.int32)
		add("Grumpy", -63)
	}

}
