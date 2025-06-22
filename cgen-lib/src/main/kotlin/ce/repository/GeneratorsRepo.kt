package ce.repository

import ce.defs.Target
import ce.domain.usecase.add.AddRegionDefaultsUseCaseImpl
import ce.formatters.CodeFormatterCxxUseCaseImpl
import ce.formatters.CodeFormatterJavaUseCaseImpl
import ce.formatters.CodeFormatterKotlinUseCaseImpl
import ce.settings.Project
import generators.cpp.CppConstantsBlockGenerator
import generators.cpp.CppDataClassGenerator
import generators.cpp.CppEnumGenerator
import generators.cpp.CppFileGenerator
import generators.cpp.CppInterfaceGenerator
import generators.java.JavaConstantsGenerator
import generators.java.JavaDataClassGenerator
import generators.java.JavaEnumGenerator
import generators.java.JavaFileGenerator
import generators.java.JavaInterfaceGenerator
import generators.kotlin.GetArrayDataTypeUseCase
import generators.kotlin.GetTypeNameUseCase
import generators.kotlin.KotlinEnumGenerator
import generators.kotlin.KotlinFileGenerator
import generators.kotlin.KotlinInterfaceGenerator
import generators.kotlin.KtConstantsGenerator
import generators.kotlin.KtDataClassGenerator
import generators.kotlin.PrepareRightValueUseCase
import generators.obj.MetaGenerator
import generators.obj.PrepareFilesListUseCaseImpl
import generators.obj.input.ConstantsBlock
import generators.obj.input.ConstantsEnum
import generators.obj.input.DataClass
import generators.obj.input.InterfaceDescription
import generators.rust.RustFileGenerator
import generators.swift.SwiftConstantsBlockGenerator
import generators.swift.SwiftDataClassGenerator
import generators.swift.SwiftEnumGenerator
import generators.swift.SwiftFileGenerator

class GeneratorsRepo(
    private val project: Project,
    private val codestylesRepo: CodestylesRepo
) {
    val supportedMeta: Map<Target, MetaGenerator>

    init {

        val targets = listOf(
            Target.Kotlin,
            Target.Cpp,
            Target.Java
        )

        val fileGeneratorsMap = mapOf(
            Target.Kotlin to KotlinFileGenerator(),
            Target.Cpp to CppFileGenerator(),
            Target.Swift to SwiftFileGenerator(),
            Target.Rust to RustFileGenerator(),
            Target.Java to JavaFileGenerator(),
        )


        val codeFormatters = mapOf(
            Target.Kotlin to CodeFormatterKotlinUseCaseImpl(codestylesRepo.get(Target.Kotlin)),
            Target.Cpp to CodeFormatterCxxUseCaseImpl(codestylesRepo.get(Target.Cpp)),
//            Target.Swift to SwiftWritter(codestylesMap[Target.Swift]!!, project.outputFolder),
//            Target.Rust to RustWritter(codestylesMap[Target.Rust]!!, project.outputFolder),
            Target.Java to CodeFormatterJavaUseCaseImpl(codestylesRepo.get(Target.Java)),
        )

        val addBlockDefaultsUseCases = targets.associate {
            it to AddRegionDefaultsUseCaseImpl(codestylesRepo.get(it))
        }

        val prepareFilesListUseCases = targets.associate {
            it to PrepareFilesListUseCaseImpl(project, fileGeneratorsMap[it]!!)
        }

        supportedMeta = targets.associate {
            val fileGenerator = fileGeneratorsMap[it]!!
            val addBlockUseCase = addBlockDefaultsUseCases[it]!!
            val generators = when (it) {
                Target.Kotlin -> {
                    val arrayDataType = GetArrayDataTypeUseCase()
                    val dataTypeToString = GetTypeNameUseCase(
                        arrayDataType = arrayDataType
                    )
                    val prepareRightValueUseCase = PrepareRightValueUseCase(
                        getTypeNameUseCase = dataTypeToString
                    )
                    mapOf(
                        ConstantsEnum::class.java to KotlinEnumGenerator(
                            addBlockUseCase,
                            dataTypeToString,
                            prepareRightValueUseCase = prepareRightValueUseCase
                        ),
                        ConstantsBlock::class.java to KtConstantsGenerator(
                            addBlockUseCase,
                            dataTypeToString = dataTypeToString,
                            prepareRightValueUseCase = prepareRightValueUseCase
                        ),
                        DataClass::class.java to KtDataClassGenerator(
                            addBlockUseCase,
                            dataTypeToString = dataTypeToString,
                            prepareRightValueUseCase = prepareRightValueUseCase
                        ),
                        InterfaceDescription::class.java to KotlinInterfaceGenerator(
                            addBlockUseCase, dataTypeToString,
                            prepareRightValueUseCase = prepareRightValueUseCase
                        )
                    )
                }

                Target.Java -> {
                    val arrayDataType = GetArrayDataTypeUseCase()
                    val dataTypeToString = GetTypeNameUseCase(
                        arrayDataType = arrayDataType
                    )
                    val prepareRightValueUseCase = PrepareRightValueUseCase(
                        getTypeNameUseCase = dataTypeToString
                    )
                    mapOf(
                        ConstantsEnum::class.java to JavaEnumGenerator(addBlockUseCase),
                        ConstantsBlock::class.java to JavaConstantsGenerator(
                            addBlockUseCase,
                            prepareRightValueUseCase = prepareRightValueUseCase
                        ),
                        DataClass::class.java to JavaDataClassGenerator(addBlockUseCase),
                        InterfaceDescription::class.java to JavaInterfaceGenerator(addBlockUseCase)
                    )
                }

                Target.Cpp -> {
                    val arrayDataType = GetArrayDataTypeUseCase()
                    val dataTypeToString = GetTypeNameUseCase(
                        arrayDataType = arrayDataType
                    )
                    val prepareRightValueUseCase = PrepareRightValueUseCase(
                        getTypeNameUseCase = dataTypeToString
                    )
                    mapOf(
                        ConstantsEnum::class.java to CppEnumGenerator(addBlockUseCase),
                        ConstantsBlock::class.java to CppConstantsBlockGenerator(
                            addBlockDefaultsUseCase = addBlockUseCase,
                            prepareRightValueUseCase = prepareRightValueUseCase),
                        DataClass::class.java to CppDataClassGenerator(addBlockUseCase),
                        InterfaceDescription::class.java to CppInterfaceGenerator(addBlockUseCase)
                    )
                }

                Target.Swift -> mapOf(
                    ConstantsEnum::class.java to SwiftEnumGenerator(fileGenerator, project),
                    ConstantsBlock::class.java to SwiftConstantsBlockGenerator(fileGenerator, project),
                    DataClass::class.java to SwiftDataClassGenerator(fileGenerator, project)
                )

                else -> throw IllegalStateException("Not supported target $it")
            }
            it to MetaGenerator(
                target = it,
                fileGenerator = fileGenerator,
                generatorsMap = generators,
                prepareFilesListUseCase = prepareFilesListUseCases[it]
                    ?: throw IllegalStateException("Can't find prepareFilesListUseCases for $it"),
                codeFormatter = codeFormatters[it] ?: throw IllegalStateException("Can't find code formatter for $it"),
            )
        }
    }

    fun get(target: Target): MetaGenerator {
        if (supportedMeta.containsKey(target))
            return supportedMeta[target]!!
        else
            throw IllegalStateException("GeneratorsRepo: Can't find $target in the $supportedMeta map")
    }
}