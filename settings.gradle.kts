listOf(
    "cgen-lib",
    "cgen-console",
    "cgen-example",
    ).forEach {
        include(":$it")
}
