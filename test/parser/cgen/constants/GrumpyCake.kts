import ce.defs.*
import generators.obj.input.*
when (target()) {
    ce.defs.Target.Kotlin -> setOutputBasePath("../kotlin/")
    ce.defs.Target.Cxx -> setOutputBasePath("../cxx/")
    else -> {}
}
namespace("com.goldman.dt1"). apply {
	constantsBlock("GrumpyCake").apply {
		addBlockComment("GrumpyCake constants definition block")
		defaultType(DataType.int64)
		add("Grumpy", -128)
		add("Silly", -128)
		add("Wild", -128)
		add("Red", -128)
	}

}
