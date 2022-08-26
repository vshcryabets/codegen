import genrators.cpp.InterfaceGeneratorCpp
import genrators.kotlin.InterfaceGeneratorKotlin
import genrators.obj.*

fun main(args: Array<String>) {
    val description = ClassDescription(
        name = "Vec2",
        namespace = "com.test",
        fields = listOf(
            ClassField("x", DataType.uint32),
            ClassField("y", DataType.uint32)
        )
    )



    val genrator = InterfaceGeneratorCpp()
    genrator.build(il5client)
}