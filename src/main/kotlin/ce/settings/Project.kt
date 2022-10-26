package ce.settings

import ce.defs.Target

data class Project(
    var files: List<String>,
    var outputFolder : String,
    var codeStyle : CodeStyle,
    var targets: List<Target> = listOf(Target.Other),
    var addAutogeneratedCommentNotification : Boolean = false,
) {
    constructor() : this(emptyList(),
        "",
        CodeStyle(1, 4,),
        emptyList(),
        false)
}