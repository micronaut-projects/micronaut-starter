# Contributing Code or Documentation to Micronaut

## Finding Issues to Work on

If you are interested in contributing to Micronaut and are looking for issues to work on, take a look at the issues tagged with [help wanted](https://github.com/micronaut-projects/micronaut-starter/issues?q=is%3Aopen+is%3Aissue+label%3A%22status%3A+help+wanted%22).

## JDK Setup

Micronaut Starter currently requires JDK 8

## IDE Setup

Micronaut Starter can be imported into IntelliJ IDEA by opening the `build.gradle` file.

## Running Tests

To run the tests use `./gradlew check`. 

## Working on the code base

### Running the CLI

To generate a project execute `./gradlew micronaut-cli:run --args=""` where the args are what would be after `mn` using the CLI. For example: `./gradlew micronaut-cli:run --args="create-app temp"`. 

If you want to test output colors or the interactive shell, the jar must be executed. Run `./gradlew micronaut-cli:shadowJar` and then `java -jar starter-cli/build/libs/micronaut-cli-2.0.0.BUILD-SNAPSHOT-all.jar create-app temp`

You can build the CLI from source by [following these instructions](https://micronaut-projects.github.io/micronaut-starter/latest/guide/index.html#installFromSource)

### Default Feature Logic

Avoid having complex logic to determine if a default feature should be applied. If, for example, a default feature should not apply if feature x is chosen, the default feature should not add logic to assert it should not apply if feature x is chosen. Instead, feature x should exclude the default feature.

### Feature Ownership

A given feature owns its templates. Any modifications that need to be made to those templates based on the existence of another feature should be the responsibility of the feature owning the template. Avoid adding arbitrary entry points for content in templates for other features to use.

### View Logic

The view layer should have the least amount of logic possible. The feature classes are where all logic should be, and the view layer should almost exclusively only check for the existence of a feature. This usually means creating additional features that are invisible to the user. Those additional features can then be checked for in other templates.

### Style Considerations

#### Avoid Empty Else

In order to preserve functionality for future additions, avoid a default else statement.

Do this:

```
if (language == Language.JAVA) {

} else if (language == Language.GROOVY) {

} else if (langauge == Language.KOTLIN) {
    //add support for kotlin
}
```

Don't do this:

```
if (language == Language.JAVA) {

} else if (language == Language.GROOVY) {

} else {
    //add support for kotlin
}
```

With the above example, when support is added for another language then the Kotlin related functionality would be applied to the new language, which of course is undesirable.

## Creating a pull request

Once you are satisfied with your changes:

- Commit your changes in your local branch
- Push your changes to your remote branch on GitHub
- Send us a [pull request](https://help.github.com/articles/creating-a-pull-request)

## Checkstyle

We want to keep the code clean, following good practices about organization, javadoc and style as much as possible. 

Micronaut Starter uses [Checkstyle](http://checkstyle.sourceforge.net/) to make sure that all the code follows those standards. The configuration file is defined in `config/checkstyle/checkstyle.xml` and to execute the Checkstyle you
need to run:
 
```
./gradlew <module-name>:checkstyleMain
```

Before start contributing with new code it is recommended to install IntelliJ [CheckStyle-IDEA](https://plugins.jetbrains.com/plugin/1065-checkstyle-idea) plugin and configure it to use Micronaut's checkstyle configuration file.
  
IntelliJ will mark in red the issues Checkstyle finds. For example:

![](https://github.com/micronaut-projects/micronaut-core/raw/master/src/main/docs/resources/img/checkstyle-issue.png)

In this case, to fix the issues, we need to:

- Add one empty line before `package` in line 16
- Add the Javadoc for the constructor in line 27
- Add an space after `if` in line 34

The plugin also adds a new tab in the bottom to run checkstyle report and see all the errors and warnings. It is recommended
to run the report and fixing all the issues before submitting a pull request.
