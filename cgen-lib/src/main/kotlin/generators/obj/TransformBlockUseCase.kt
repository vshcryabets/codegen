package generators.obj

import generators.obj.input.Block
import generators.obj.out.FileData

interface TransformBlockUseCase<I: Block> {
    operator fun invoke(blockFiles: List<FileData>, desc: I)
}