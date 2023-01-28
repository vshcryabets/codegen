package ce.domain.usecase.transform

import ce.defs.Target
import ce.defs.globRootNamespace
import ce.defs.namespaceMap
import ce.repository.GeneratorsRepo
import ce.settings.Project
import generators.cpp.*
import generators.java.*
import generators.kotlin.*
import generators.obj.Generator
import generators.obj.MetaGenerator
import generators.obj.input.*
import generators.obj.out.ProjectOutput
import generators.rust.*
import generators.swift.*

class TransformInTreeToOutTreeUseCase(val generatorsRepo: GeneratorsRepo) {
    operator fun invoke(inTree: Node, target: Target) : ProjectOutput {
        val meta = generatorsRepo.get(target)

        // build output tree and generate code
        return meta!!.processProject(inTree, namespaceMap)
    }
}