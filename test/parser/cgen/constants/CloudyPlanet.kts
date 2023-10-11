namespace("com.goldman.dt1"). apply {
	constantsBlock("CloudyPlanet").apply {
		addBlockComment("CloudyPlanet constants definition block")
		defaultType(DataType.uint32)
		add("Grumpy", 128)
		add("Silly", 128)
		add("Wild", 128)
		add("Red", 128)
		add("Brown", 128)
	}
}