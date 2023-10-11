namespace("com.goldman.dt1"). apply {
	constantsBlock("GrumpyDog").apply {
		addBlockComment("GrumpyDog constants definition block")
		defaultType(DataType.int16)
		add("Grumpy", -32)
		add("Silly", -32)
	}
}
