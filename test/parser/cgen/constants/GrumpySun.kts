import ce.defs.*
import generators.obj.input.*
when (target()) {
    ce.defs.Target.Kotlin -> setOutputBasePath("../kotlin/")
    ce.defs.Target.Cxx -> setOutputBasePath("../cxx/")
    else -> {}
}
namespace("com.goldman.dt1"). apply {
	constantsBlock("GrumpySun").apply {
		addBlockComment("GrumpySun constants definition block")
		defaultType(DataType.float64)
		add("Grumpy", 23.91)
		add("Silly", 23.91)
		add("Wild", 23.91)
		add("Red", 23.91)
		add("Brown", 23.91)
		add("Tasty", 23.91)
		add("Wise", 23.91)
		add("Windy", 23.91)
		add("Cloudy", 23.91)
		add("Noble", 23.91)
	}

}
