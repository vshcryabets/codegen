namespace("com.goldman.dt1"). apply {
	constantsBlock("WindyPlanet").apply {
		addBlockComment("WindyPlanet constants definition block")
		defaultType(DataType.uint16)
		add("Grumpy", 32)
		add("Silly", 32)
		add("Wild", 32)
		add("Red", 32)
	}
}
