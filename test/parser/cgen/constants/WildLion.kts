import ce.defs.*
import generators.obj.input.*
when (target()) {
    ce.defs.Target.Kotlin -> setOutputBasePath("../kotlin/")
    ce.defs.Target.Cxx -> setOutputBasePath("../cxx/")
    else -> {}
}
namespace("com.goldman.dt1"). apply {
	constantsBlock("WildLion").apply {
		addBlockComment("WildLion constants definition block")
		defaultType(DataType.float32)
		add("Grumpy", 23.45)
		add("Silly", 23.45)
		add("Wild", 23.45)
		add("Red", 23.45)
		add("Brown", 23.45)
		add("Tasty", 23.45)
		add("Wise", 23.45)
		add("Windy", 23.45)
		add("Cloudy", 23.45)
	}

}
