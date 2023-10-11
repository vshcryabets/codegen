namespace("com.goldman.dt1"). apply {
	constantsBlock("TastyPlanet").apply {
		addBlockComment("TastyPlanet constants definition block")
		defaultType(DataType.int64)
		add("Grumpy", -128)
		add("Silly", -128)
	}
}
