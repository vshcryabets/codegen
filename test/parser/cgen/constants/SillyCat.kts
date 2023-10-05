import ce.defs.*
import generators.obj.input.*
when (target()) {
    ce.defs.Target.Kotlin -> setOutputBasePath("../kotlin/")
    ce.defs.Target.Cxx -> setOutputBasePath("../cxx/")
    else -> {}
}
namespace("com.goldman.dt1"). apply {
	constantsBlock("SillyCat").apply {
		addBlockComment("SillyCat constants definition block")
		defaultType(DataType.int16)
		add("Grumpy", -32)
		add("Silly", -32)
	}

}
