namespace("com.goldman.dt1"). apply {
	constantsBlock("AngryFish").apply {
		addBlockComment("AngryFish constants definition block")
		defaultType(DataType.int32)
		add("Grumpy", -63)
	}
}
