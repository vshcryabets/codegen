package genrators.kotlin

import genrators.obj.ClassDescription

interface ClassGenerator {
    fun build(description: ClassDescription)
}