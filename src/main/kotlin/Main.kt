import genrators.cpp.InterfaceGeneratorCpp
import genrators.kotlin.ClassGeneratorKotlin
import genrators.kotlin.ConstantsObjectGenrator
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

    val constans = ConstantsEnum(
        name = "errors",
        namespace = "space",
        constants = listOf(
            ClassField("ok", DataType.uint16, value = 0),
            ClassField("a", DataType.uint16, value = 1),
            ClassField("b", DataType.uint16, value = 2),
            ClassField("c", DataType.uint16, value = 3)
        )
    )

    val generator = ConstantsObjectGenrator()
    println(generator.build(constans).toString())
}