import ce.defs.*
import generators.obj.input.*
when (target()) {
    ce.defs.Target.Kotlin -> setOutputBasePath("../kotlin/")
    ce.defs.Target.Cxx -> setOutputBasePath("../cxx/")
    else -> {}
}
namespace("com.goldman.dt1"). apply {
	constantsBlock("CloudyCake").apply {
		addBlockComment("CloudyCake constants definition block")
		defaultType(DataType.int16)
		add("Grumpy", -32)
		add("Silly", -32)
		add("Wild", -32)
		add("Red", -32)
		add("Brown", -32)
		add("Tasty", -32)
		add("Wise", -32)
		add("Windy", -32)
		add("Cloudy", -32)
		add("Noble", -32)
		add("Angry", -32)
		add("Crazy", -32)
	}

}
