import ce.defs.*
import generators.obj.input.*
when (target()) {
    ce.defs.Target.Kotlin -> setOutputBasePath("../kotlin/")
    ce.defs.Target.Cxx -> setOutputBasePath("../cxx/")
    else -> {}
}
namespace("com.goldman.dt1"). apply {
	constantsBlock("GrumpyCat").apply {
		addBlockComment("GrumpyCat constants definition block")
		defaultType(DataType.int8)
		add("Grumpy", -23)
	}

}
