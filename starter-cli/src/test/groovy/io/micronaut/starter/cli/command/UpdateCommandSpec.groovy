package io.micronaut.starter.cli.command

import io.micronaut.starter.cli.util.openrewrite.OpenRewriteConfiguration
import io.micronaut.starter.cli.util.openrewrite.OpenRewriteRecipesRunner
import spock.lang.Specification
import spock.lang.TempDir

class UpdateCommandSpec extends Specification {

    @TempDir
    File tmpDir

    def "uses Gradle runner when build.gradle is present"() {
        given:
        File gradleProject = new File(tmpDir, "gradle-app")
        gradleProject.mkdirs()
        new File(gradleProject, "build.gradle").text = "// dummy gradle build"

        def gradleRunner = new CapturingRunner()
        def mavenRunner = new CapturingRunner()
        def cmd = new UpdateCommand(gradleRunner, mavenRunner)
        cmd.projectDir = gradleProject

        when:
        Integer exitCode = cmd.call()

        then:
        exitCode == 0
        gradleRunner.calls == 1
        mavenRunner.calls == 0
        gradleRunner.folder.canonicalFile == gradleProject.canonicalFile
        gradleRunner.recipes.contains("io.micronaut.openrewrite.update")
        gradleRunner.configuration != null
        gradleRunner.configuration.exportDatatables()
        gradleRunner.configuration.recipeChangeLogLevel() == "INFO"
    }

    def "defaults to current directory when --project is not provided (Maven project)"() {
        given:
        File mavenProject = new File(tmpDir, "maven-app")
        mavenProject.mkdirs()
        new File(mavenProject, "pom.xml").text = "<!-- dummy pom -->"

        def gradleRunner = new CapturingRunner()
        def mavenRunner = new CapturingRunner()
        def cmd = new UpdateCommand(gradleRunner, mavenRunner)
        cmd.projectDir = null

        String oldUserDir = System.getProperty("user.dir")
        System.setProperty("user.dir", mavenProject.absolutePath)

        when:
        Integer exitCode = cmd.call()

        then:
        exitCode == 0
        mavenRunner.calls == 1
        gradleRunner.calls == 0
        mavenRunner.folder.canonicalFile == mavenProject.canonicalFile
        mavenRunner.recipes.contains("io.micronaut.openrewrite.update")

        cleanup:
        System.setProperty("user.dir", oldUserDir)
    }

    def "uses Gradle runner when settings.gradle is present"() {
        given:
        File project = new File(tmpDir, "settings-gradle-app")
        project.mkdirs()
        new File(project, "settings.gradle").text = "// dummy settings"

        def gradleRunner = new CapturingRunner()
        def mavenRunner = new CapturingRunner()
        def cmd = new UpdateCommand(gradleRunner, mavenRunner)
        cmd.projectDir = project

        when:
        Integer exitCode = cmd.call()

        then:
        exitCode == 0
        gradleRunner.calls == 1
        mavenRunner.calls == 0
        gradleRunner.folder.canonicalFile == project.canonicalFile
        gradleRunner.recipes.contains("io.micronaut.openrewrite.update")
    }

    def "uses Gradle runner when build.gradle.kts is present"() {
        given:
        File project = new File(tmpDir, "gradle-kts-app")
        project.mkdirs()
        new File(project, "build.gradle.kts").text = "// dummy gradle kts build"

        def gradleRunner = new CapturingRunner()
        def mavenRunner = new CapturingRunner()
        def cmd = new UpdateCommand(gradleRunner, mavenRunner)
        cmd.projectDir = project

        when:
        Integer exitCode = cmd.call()

        then:
        exitCode == 0
        gradleRunner.calls == 1
        mavenRunner.calls == 0
        gradleRunner.folder.canonicalFile == project.canonicalFile
        gradleRunner.recipes.contains("io.micronaut.openrewrite.update")
    }

    def "prefers Maven when pom.xml and Gradle files both exist"() {
        given:
        File project = new File(tmpDir, "mixed-build-app")
        project.mkdirs()
        new File(project, "pom.xml").text = "<project/>"
        new File(project, "build.gradle").text = "// gradle present too"

        def gradleRunner = new CapturingRunner()
        def mavenRunner = new CapturingRunner()
        def cmd = new UpdateCommand(gradleRunner, mavenRunner)
        cmd.projectDir = project

        when:
        Integer exitCode = cmd.call()

        then:
        exitCode == 0
        mavenRunner.calls == 1
        gradleRunner.calls == 0
        mavenRunner.folder.canonicalFile == project.canonicalFile
        mavenRunner.recipes.contains("io.micronaut.openrewrite.update")
    }

    def "returns 3 when no build tool is detected"() {
        given:
        File project = new File(tmpDir, "no-build-tool")
        project.mkdirs()

        def gradleRunner = new CapturingRunner()
        def mavenRunner = new CapturingRunner()
        def cmd = new UpdateCommand(gradleRunner, mavenRunner)
        cmd.projectDir = project

        when:
        Integer exitCode = cmd.call()

        then:
        exitCode == 3
        gradleRunner.calls == 0
        mavenRunner.calls == 0
    }

    def "returns 2 when projectDir is invalid (non-existent)"() {
        given:
        File project = new File(tmpDir, "missing-dir") // do not create

        def gradleRunner = new CapturingRunner()
        def mavenRunner = new CapturingRunner()
        def cmd = new UpdateCommand(gradleRunner, mavenRunner)
        cmd.projectDir = project

        when:
        Integer exitCode = cmd.call()

        then:
        exitCode == 2
        gradleRunner.calls == 0
        mavenRunner.calls == 0
    }

    def "returns 2 when projectDir is a file (not a directory)"() {
        given:
        File file = new File(tmpDir, "just-a-file.txt")
        file.parentFile.mkdirs()
        file.text = "data"

        def gradleRunner = new CapturingRunner()
        def mavenRunner = new CapturingRunner()
        def cmd = new UpdateCommand(gradleRunner, mavenRunner)
        cmd.projectDir = file

        when:
        Integer exitCode = cmd.call()

        then:
        exitCode == 2
        gradleRunner.calls == 0
        mavenRunner.calls == 0
    }

    private static class CapturingRunner implements OpenRewriteRecipesRunner {
        int calls = 0
        List<String> recipes
        File folder
        OpenRewriteConfiguration configuration

        @Override
        void run(List<String> recipes,
                 File folder,
                 OpenRewriteConfiguration configuration,
                 java.util.function.Consumer<String> out,
                 java.util.function.Consumer<String> err) {
            this.calls++
            this.recipes = recipes
            this.folder = folder
            this.configuration = configuration
        }
    }
}
