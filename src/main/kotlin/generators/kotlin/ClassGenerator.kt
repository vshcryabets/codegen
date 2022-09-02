package generators.kotlin

import generators.obj.input.ClassDescription

interface ClassGenerator {
    fun build(description: ClassDescription)
}