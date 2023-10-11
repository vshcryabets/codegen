namespace("com.goldman.dt1"). apply {
	constantsBlock("WildCar").apply {
		addBlockComment("WildCar constants definition block")
		defaultType(DataType.uint64)
		add("Grumpy", 243)
		add("Silly", 243)
		add("Wild", 243)
		add("Red", 243)
		add("Brown", 243)
		add("Tasty", 243)
		add("Wise", 243)
		add("Windy", 243)
	}
}
