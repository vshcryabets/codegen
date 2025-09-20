package generators.java

import ce.domain.usecase.add.AddRegionDefaultsUseCase
import generators.obj.TransformBlockUseCase
import generators.obj.abstractSyntaxTree.DataClass
import generators.obj.abstractSyntaxTree.DataField
import generators.obj.abstractSyntaxTree.addDatatype
import generators.obj.abstractSyntaxTree.addOutBlock
import generators.obj.abstractSyntaxTree.addOutBlockArguments
import generators.obj.abstractSyntaxTree.addSub
import generators.obj.abstractSyntaxTree.addVarName
import generators.obj.syntaxParseTree.ArgumentNode
import generators.obj.syntaxParseTree.FileData
import generators.obj.syntaxParseTree.RegionImpl

class JavaDataClassGenerator(
    private val addBlockDefaultsUseCase: AddRegionDefaultsUseCase,
) : TransformBlockUseCase<DataClass> {

    override fun invoke(blockFiles: List<FileData>, desc: DataClass) {
        val file = blockFiles.firstOrNull()
            ?: throw java.lang.IllegalStateException("Can't find Main file for $desc")

        file.addSub(RegionImpl()).apply {
            addBlockDefaultsUseCase(desc, this)
            addOutBlock("public record ${desc.name}") {
                addOutBlockArguments {
                    desc.subs.forEach { leaf ->
                        if (leaf is DataField) {
                            addSub(ArgumentNode().apply {
                                addDatatype(Types.typeTo(file, leaf.getType()))
                                addVarName(leaf.name)
                            })
                        }
                    }
                }
            }
        }
    }
}
