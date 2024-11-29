package ce.repository

import ce.defs.Target
import ce.defs.TargetConfiguration
import generators.cpp.CppWritter
import generators.java.JavaWritter
import generators.kotlin.KotlinWritter
import generators.obj.Writter

interface WrittersRepo {
    fun getWritter(targetConfiguration: TargetConfiguration): Writter
}

class WrittersRepoImpl(
    private val codestylesRepo: CodestylesRepo
) : WrittersRepo {
    override fun getWritter(targetConfiguration: TargetConfiguration): Writter {
        return when(targetConfiguration.type) {
            Target.Kotlin -> KotlinWritter(codestylesRepo.get(Target.Kotlin), targetConfiguration.outputFolder)
            Target.Cxx -> CppWritter(codestylesRepo.get(Target.Cxx), targetConfiguration.outputFolder)
//            Target.Swift to SwiftWritter(codestylesMap[Target.Swift]!!, project.outputFolder),
//            Target.Rust to RustWritter(codestylesMap[Target.Rust]!!, project.outputFolder),
            Target.Java -> JavaWritter(codestylesRepo.get(targetConfiguration.type), targetConfiguration.outputFolder)
            else -> throw IllegalStateException("Unsuported target (${targetConfiguration.type}) for WrittersFactoryImpl")
        }
    }

}