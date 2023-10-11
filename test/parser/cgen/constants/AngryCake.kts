namespace("com.goldman.dt1"). apply {
	constantsBlock("AngryCake").apply {
		addBlockComment("AngryCake constants definition block")
		defaultType(DataType.int64)
		add("Grumpy", -128)
		add("Silly", -128)
	}
}
