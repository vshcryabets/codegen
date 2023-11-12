namespace("com.goldman.dt1"). apply {
	constantsBlock("CrazyDog").apply {
		addBlockComment("CrazyDog constants definition block")
		defaultType(DataType.int32)
		add("Grumpy", -63)
	}
}
