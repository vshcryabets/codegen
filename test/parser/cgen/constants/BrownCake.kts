import ce.defs.*
import generators.obj.input.*
when (target()) {
    ce.defs.Target.Kotlin -> setOutputBasePath("../kotlin/")
    ce.defs.Target.Cxx -> setOutputBasePath("../cxx/")
    else -> {}
}
namespace("com.goldman.dt1"). apply {
	constantsBlock("BrownCake").apply {
		addBlockComment("BrownCake constants definition block")
		defaultType(DataType.uint64)
		add("Grumpy", 243)
		add("Silly", 243)
		add("Wild", 243)
		add("Red", 243)
		add("Brown", 243)
		add("Tasty", 243)
		add("Wise", 243)
		add("Windy", 243)
	}

}
