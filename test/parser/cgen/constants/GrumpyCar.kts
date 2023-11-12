namespace("com.goldman.dt1"). apply {
	constantsBlock("GrumpyCar").apply {
		addBlockComment("GrumpyCar constants definition block")
		defaultType(DataType.uint16)
		add("Grumpy", 32)
		add("Silly", 32)
		add("Wild", 32)
		add("Red", 32)
		add("Brown", 32)
		add("Tasty", 32)
	}
}
