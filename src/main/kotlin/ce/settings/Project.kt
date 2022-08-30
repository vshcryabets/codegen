package ce.settings

data class Project(
    val enumFiles: List<String>,
    val outputFolder : String,
    val codeStyle : CodeStyle
)