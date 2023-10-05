import ce.defs.*
import generators.obj.input.*
when (target()) {
    ce.defs.Target.Kotlin -> setOutputBasePath("../kotlin/")
    ce.defs.Target.Cxx -> setOutputBasePath("../cxx/")
    else -> {}
}
namespace("com.goldman.dt1"). apply {
	constantsBlock("WiseDoor").apply {
		addBlockComment("WiseDoor constants definition block")
		defaultType(DataType.int8)
		add("Grumpy", -23)
		add("Silly", -23)
		add("Wild", -23)
		add("Red", -23)
		add("Brown", -23)
		add("Tasty", -23)
		add("Wise", -23)
		add("Windy", -23)
		add("Cloudy", -23)
		add("Noble", -23)
		add("Angry", -23)
	}

}
