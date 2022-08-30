package generators.kotlin

import generators.obj.ClassDescription

interface ClassGenerator {
    fun build(description: ClassDescription)
}