namespace("com.goldman.dt1"). apply {
	constantsBlock("WisePlanet").apply {
		addBlockComment("WisePlanet constants definition block")
		defaultType(DataType.uint8)
		add("Grumpy", 23)
		add("Silly", 23)
		add("Wild", 23)
	}
}
