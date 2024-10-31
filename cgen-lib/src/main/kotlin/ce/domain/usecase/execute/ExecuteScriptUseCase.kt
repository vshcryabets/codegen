package ce.domain.usecase.execute

import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import javax.inject.Inject
import javax.script.ScriptEngine
import javax.script.ScriptException

interface ExecuteScriptUseCase {
    @Throws(ScriptException::class)
    operator fun invoke(file: File)
}

class ExecuteScriptByExtUseCaseImpl @Inject constructor(
    private val ktsScriptEngine: ScriptEngine,
    private val groovyScriptEngine: ScriptEngine,
) : ExecuteScriptUseCase {
    @Throws(ScriptException::class)
    override fun invoke(file: File) {
        if (file.name.endsWith(".kts", true)) {
            exec(file, ktsScriptEngine)
        } else if (file.name.endsWith(".groovy", true)) {
            exec(file, groovyScriptEngine)
        } else {
            throw ScriptException("Unknown file type ${file.name}")
        }
    }

    private fun exec(file: File, scriptEngine: ScriptEngine) {
        val reader = InputStreamReader(FileInputStream(file))
        try {
            scriptEngine.eval(reader)
        } catch (error: ScriptException) {
            error("Error in file ${file.absoluteFile}:${error.message}")
            throw error
        }
    }
}