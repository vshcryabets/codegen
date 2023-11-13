# codegen
code generation experiments

## Prepare docs
Prepare Input tree:

`./gradlew :cgen-console:execute -PclassToExecute=ce.entrypoints.PrepareInTreeKt --args='./docs/sample_project.json'`
 
Build input tree diagram:

`./gradlew :cgen-console:execute -PclassToExecute=ce.entrypoints.DrawInTreeKt --args='./generated/input_tree_Kotlin.xml ./docs/kotling_input_tree.svg'`

Prepare Output (AST) tree:

`./gradlew :cgen-console:execute -PclassToExecute=ce.entrypoints.BuildOutTreeKt --args='./generated/input_tree_Kotlin.xml ./docs/sample_project.json ./generated/output_tree_Kotlin.xml Kotlin'`

Build output (AST) tree diagram:

`./gradlew :cgen-console:execute -PclassToExecute=ce.entrypoints.DrawOutTreeKt --args='./generated/output_tree_Kotlin.xml ./docs/kotling_output_tree.svg'`


## Run tests
- `./gradlew test`

## Run build project test
- `./gradlew run -PclassToExecute=ce.entrypoints.BuildProjectKt`
