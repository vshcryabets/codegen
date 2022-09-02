package generators.obj

import ce.defs.Target

data class MetaGenerator(
    val target: Target = Target.Other,
    val enum : Generator,
    val constantsBlock : Generator,
    val writter: Writter
)