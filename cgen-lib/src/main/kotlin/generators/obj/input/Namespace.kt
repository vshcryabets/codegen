package generators.obj.input

import ce.defs.customBaseFolderPath
import ce.defs.outputFile
import ce.defs.sourceFile

open class Namespace(name: String, parent: Node) : Node(name, parent) {
    fun getNamespace(name: String): Namespace {
        if (name.isEmpty()) {
            return this
        }
        val pointPos = name.indexOf('.')
        val searchName: String
        val endPath: String
        if (pointPos < 0) {
            searchName = name
            endPath = ""
        } else {
            searchName = name.substring(0, pointPos)
            endPath = name.substring(pointPos + 1)
        }

        subs.forEach {
            if (it is Namespace) {
                if (it.name == searchName) {
                    return it.getNamespace(endPath)
                }
            }
        }
        val newNamaspace = Namespace(searchName, this)
        subs.add(newNamaspace)
        return newNamaspace.getNamespace(endPath)
    }

    fun putDefaults(block: Block) {
        this.addSub(block)
        block.objectBaseFolder = customBaseFolderPath
        block.sourceFile = sourceFile
        block.outputFile = if (outputFile.isEmpty()) block.name else outputFile
        println("Block ${block.name} = ${block.outputFile}");
    }

    fun enum(name: String): ConstantsEnum {
        return ConstantsEnum(name, this).apply {
            putDefaults(this)
        }
    }

    fun constantsBlock(name: String): ConstantsBlock {
        return ConstantsBlock(name, this).apply {
            putDefaults(this)
        }
    }

    fun dataClass(name: String): DataClass {
        return DataClass(name, this)
            .apply {
                putDefaults(this)
            }
    }

    fun declareInterface(name: String): InterfaceDescription {
        return InterfaceDescription(name, this)
            .apply {
                putDefaults(this)
            }
    }
}
