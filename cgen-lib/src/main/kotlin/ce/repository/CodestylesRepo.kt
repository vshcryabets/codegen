package ce.repository

import ce.formatters.CLikeCodestyleRepo
import ce.formatters.CodeStyleRepo
import ce.settings.Project

interface CodestylesRepo {
    fun get(target: ce.defs.Target): CodeStyleRepo
}

class CodestyleRepoImpl(
    private val project: Project
): CodestylesRepo {
    override fun get(target: ce.defs.Target): CodeStyleRepo {
        return CLikeCodestyleRepo(project.codeStyle)
    }

}