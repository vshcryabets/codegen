namespace("com.goldman.dt1"). apply {
	constantsBlock("GrumpyCake").apply {
		addBlockComment("GrumpyCake constants definition block")
		defaultType(DataType.int64)
		add("Grumpy", -128)
		add("Silly", -128)
		add("Wild", -128)
		add("Red", -128)
	}
}
