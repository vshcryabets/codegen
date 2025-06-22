package ce.repository

interface ReportsRepo {
    fun logd(line: String)
    fun loge(line: String)
    fun logw(line: String)
    fun logi(line: String)
}

class ReportsRepoImpl : ReportsRepo {
    override fun logd(line: String) {
        println("DEBUG: $line")
    }

    override fun loge(line: String) {
        println("ERROR: $line")
    }

    override fun logw(line: String) {
        println("WARN: $line")
    }

    override fun logi(line: String) {
        println("INFO: $line")
    }
}