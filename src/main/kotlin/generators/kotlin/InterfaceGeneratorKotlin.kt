package generators.kotlin

import generators.obj.InterfaceDescription

class InterfaceGeneratorKotlin  {
    fun build(desc: InterfaceDescription) {
        println("");
        println("interface ${desc.name} {");
        desc.publicMethods.forEach {
            print("    ");
            print("fun ${it.name}(")
            it.arguments.forEach {
                print("${it.name} : ${it.datatype}, ")
            }
            print(") : ${it.result}")
            println()
        }
        println("}");
    }
}