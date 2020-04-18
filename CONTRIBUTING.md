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

To generate a project execute `./gradlew run --args=""` where the args are what would be after `mn` using the CLI. For example: `./gradlew run --args="create-app temp"`. 

If you want to test output colors or the interactive shell, the jar must be executed. Run `./gradlew assemble` and then `java -jar starter-core/build/libs/starter-core-1.0.0.BUILD-SNAPSHOT.jar create-app temp`

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