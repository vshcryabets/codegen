package ce.settings

import ce.defs.Target

data class Project(
    val files: List<String>,
    val outputFolder : String,
    val codeStyle : CodeStyle,
    val targets: List<Target> = listOf(Target.Other),
    val addAutogeneratedCommentNotification : Boolean = false,
)