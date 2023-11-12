namespace("com.goldman.dt1"). apply {
	constantsBlock("WindyCar").apply {
		addBlockComment("WindyCar constants definition block")
		defaultType(DataType.int32)
		add("Grumpy", -63)
	}
}
