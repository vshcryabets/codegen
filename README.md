# codegen
code generation experiments

## Prepare docs
Prepare AST tree:

`./gradlew :cgen-console:execute -PclassToExecute=ce.entrypoints.PrepareAstTreeKt --args='./docs/sample_project.json'`
 
Build AST tree diagram:

`./gradlew :cgen-console:execute -PclassToExecute=ce.entrypoints.DrawAstTreeKt --args='./generated/ast_tree_Kotlin.xml ./docs/kotling_ast_tree.svg'`

Prepare Output (Parse) tree:

`./gradlew :cgen-console:execute -PclassToExecute=ce.entrypoints.BuildOutTreeKt --args='./generated/ast_tree_Kotlin.xml ./docs/sample_project.json ./generated/output_tree_Kotlin.xml Kotlin'`

Build output (Parse) tree diagram:

`./gradlew :cgen-console:execute -PclassToExecute=ce.entrypoints.DrawOutTreeKt --args='./generated/output_tree_Kotlin.xml ./docs/kotlin_output_tree.svg'`

Prepare code styled output (Parse) tree:

`./gradlew :cgen-console:execute -PclassToExecute=ce.entrypoints.BuildCodeStyleTreeKt --args='./generated/output_tree_Kotlin.xml ./docs/sample_project.json  ./generated/codestyle_output_tree_Kotlin.xml'`

Build code styled output (Parse) tree:

`./gradlew :cgen-console:execute -PclassToExecute=ce.entrypoints.DrawCodeStyleTreeKt --args='./generated/codestyle_output_tree_Kotlin.xml ./docs/kotlin_codestyle_tree.svg'`


## Run tests
- `./gradlew test`

## Run build project test
- `./gradlew run -PclassToExecute=ce.entrypoints.BuildProjectKt`
