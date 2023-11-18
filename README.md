# codegen
code generation experiments

## Prepare docs
- `./gradlew :cgen-console:execute -PclassToExecute=ce.entrypoints.PrepareInTreeKt --args='./docs/sample_project.json'`

## Run tests
- `./gradlew test`

## Run build project test
- `./gradlew run -PclassToExecute=ce.entrypoints.BuildProjectKt`
