namespace("com.goldman.dt1"). apply {
	constantsBlock("RedSun").apply {
		addBlockComment("RedSun constants definition block")
		defaultType(DataType.int32)
		add("Grumpy", -63)
	}
}
