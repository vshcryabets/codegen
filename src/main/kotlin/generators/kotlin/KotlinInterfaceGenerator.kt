package generators.kotlin

import generators.obj.input.InterfaceDescription

class KotlinInterfaceGenerator  {
    fun build(desc: InterfaceDescription) {
        println("");
        println("interface ${desc.name} {");
        desc.subs.forEach {
//            print("    ");
//            print("fun ${it.name}(")
//            it.arguments.forEach {
//                print("${it.name} : ${it.datatype}, ")
//            }
//            print(") : ${it.result}")
//            println()
        }
        println("}");
    }
}