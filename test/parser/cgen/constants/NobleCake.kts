namespace("com.goldman.dt1"). apply {
	constantsBlock("NobleCake").apply {
		addBlockComment("NobleCake constants definition block")
		defaultType(DataType.int32)
		add("Grumpy", -63)
	}
}
