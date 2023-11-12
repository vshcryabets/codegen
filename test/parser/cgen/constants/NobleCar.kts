namespace("com.goldman.dt1"). apply {
	constantsBlock("NobleCar").apply {
		addBlockComment("NobleCar constants definition block")
		defaultType(DataType.uint8)
		add("Grumpy", 23)
		add("Silly", 23)
		add("Wild", 23)
	}
}
