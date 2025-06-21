package generators.obj.out

import org.junit.jupiter.api.Test

class ImportsBlockTests {
    @Test
    fun testImportsBlockShouldntHaveSameIncludes() {
        val importsBlock = ImportsBlock()
        importsBlock.addInclude("<iostream>")
        importsBlock.addInclude("<string>")
        importsBlock.addInclude("<vector>")
        importsBlock.addInclude("<iostream>") // Duplicate include, should not be added again

        assert(importsBlock.subs.size == 3) { "Imports block should contain only unique includes." }
        assert(importsBlock.subs.any { it.name == "<iostream>" }) { "Imports block should contain <iostream> include." }
    }
}