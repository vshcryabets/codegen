namespace("com.goldman.dt1"). apply {
	constantsBlock("TastySun").apply {
		addBlockComment("TastySun constants definition block")
		defaultType(DataType.uint8)
		add("Grumpy", 23)
		add("Silly", 23)
		add("Wild", 23)
	}
}
