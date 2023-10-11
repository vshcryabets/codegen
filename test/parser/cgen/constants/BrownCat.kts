namespace("com.goldman.dt1"). apply {
	constantsBlock("BrownCat").apply {
		addBlockComment("BrownCat constants definition block")
		defaultType(DataType.uint8)
		add("Grumpy", 23)
		add("Silly", 23)
		add("Wild", 23)
		add("Red", 23)
		add("Brown", 23)
	}
}
