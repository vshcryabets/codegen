package ce.entrypoints

import ce.parser.domain.JsonToDataClassUseCaseImpl

fun main(args: Array<String>) {
    if (args.size < 1) {
        error("Please specify json file!")
    }
    val jsonToData = JsonToDataClassUseCaseImpl()
}
