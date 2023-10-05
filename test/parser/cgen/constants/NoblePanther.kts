import ce.defs.*
import generators.obj.input.*
when (target()) {
    ce.defs.Target.Kotlin -> setOutputBasePath("../kotlin/")
    ce.defs.Target.Cxx -> setOutputBasePath("../cxx/")
    else -> {}
}
namespace("com.goldman.dt1"). apply {
	constantsBlock("NoblePanther").apply {
		addBlockComment("NoblePanther constants definition block")
		defaultType(DataType.uint32)
		add("Grumpy", 128)
		add("Silly", 128)
		add("Wild", 128)
		add("Red", 128)
		add("Brown", 128)
	}

}
