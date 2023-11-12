namespace("com.goldman.dt1"). apply {
	constantsBlock("GrumpyCat").apply {
		addBlockComment("GrumpyCat constants definition block")
		defaultType(DataType.int8)
		add("Grumpy", -23)
	}
}
