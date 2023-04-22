package io.micronaut.starter.core.test.feature.dekorate

import io.micronaut.starter.feature.Feature
import io.micronaut.starter.feature.dekorate.AbstractDekoratePlatformFeature
import io.micronaut.starter.feature.dekorate.AbstractDekorateServiceFeature
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.BuildToolTest
import io.micronaut.starter.test.CommandSpec
import spock.lang.IgnoreIf
import spock.lang.Unroll

import java.nio.file.Files
import java.nio.file.Paths

class DekorateSpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        return "dekorate"
    }

    @IgnoreIf({ BuildToolTest.IGNORE_MAVEN })
    @Unroll
    void "test maven dekorate platform #feature.name with #language"(Feature feature, Language language) {
        when:
        generateProject(language, BuildTool.MAVEN, [feature.getName()])
        String output =  executeMaven("compile")
        String manifestName = feature.getName().replaceFirst("dekorate-", "")

        then:
        output?.contains("BUILD SUCCESS")
        Files.exists(Paths.get(dir.getPath(),"target/classes", String.format("META-INF/dekorate/%s.json", manifestName)))
        Files.exists(Paths.get(dir.getPath(),"target/classes", String.format("META-INF/dekorate/%s.json", manifestName)))

        where:
        [feature, language] << [
                beanContext.getBeansOfType(AbstractDekoratePlatformFeature),
                [Language.JAVA, Language.KOTLIN]].combinations()
    }

    @IgnoreIf({ BuildToolTest.IGNORE_MAVEN })
    @Unroll
    void "test maven dekorate service #feature.name with #language on default platform"(Feature feature, Language language) {
        when:
        generateProject(language, BuildTool.MAVEN, [feature.getName()])
        String output =  executeMaven("compile")

        then:
        output?.contains("BUILD SUCCESS")
        Files.exists(Paths.get(dir.getPath(),"target/classes", "META-INF/dekorate/kubernetes.json"))
        Files.exists(Paths.get(dir.getPath(),"target/classes", "META-INF/dekorate/kubernetes.json"))

        where:
        [feature, language] << [
                beanContext.getBeansOfType(AbstractDekorateServiceFeature),
                [Language.JAVA, Language.KOTLIN]].combinations()
    }

    @Unroll
    void "test gradle dekorate platform #feature.name with #language"(Feature feature, Language language) {
        when:
        generateProject(language, BuildTool.GRADLE, [feature.getName()])
        String output =  executeGradle("compileJava")?.output
        String manifestName = feature.getName().replaceFirst("dekorate-", "")

        then:
        output?.contains("BUILD SUCCESS")
        if(language == Language.KOTLIN){
            assert Files.exists(Paths.get(dir.getPath(),"build/tmp/kapt3/classes/main", String.format("META-INF/dekorate/%s.json", manifestName)))
            assert Files.exists(Paths.get(dir.getPath(),"build/tmp/kapt3/classes/main", String.format("META-INF/dekorate/%s.json", manifestName)))
        }else{
            assert Files.exists(Paths.get(dir.getPath(),"build/classes/java/main", String.format("META-INF/dekorate/%s.json", manifestName)))
            assert Files.exists(Paths.get(dir.getPath(),"build/classes/java/main", String.format("META-INF/dekorate/%s.json", manifestName)))
        }

        where:
        [feature, language] << [
                beanContext.getBeansOfType(AbstractDekoratePlatformFeature),
                [Language.JAVA, Language.KOTLIN]].combinations()
    }

    @Unroll
    void "test gradle dekorate service #feature.name with #language on default platform"(Feature feature, Language language) {
        when:
        generateProject(language, BuildTool.GRADLE, [feature.getName()])
        String output =  executeGradle("compileJava")?.output

        then:
        output?.contains("BUILD SUCCESS")
        if (language == Language.KOTLIN) {
            assert Files.exists(Paths.get(dir.getPath(), "build/tmp/kapt3/classes/main", "META-INF/dekorate/kubernetes.json"))
            assert Files.exists(Paths.get(dir.getPath(), "build/tmp/kapt3/classes/main", "META-INF/dekorate/kubernetes.json"))
        } else {
            assert Files.exists(Paths.get(dir.getPath(), "build/classes/java/main", "META-INF/dekorate/kubernetes.json"))
            assert Files.exists(Paths.get(dir.getPath(), "build/classes/java/main", "META-INF/dekorate/kubernetes.json"))
        }

        where:
        [feature, language] << [
                beanContext.getBeansOfType(AbstractDekorateServiceFeature),
                [Language.JAVA, Language.KOTLIN]].combinations()
    }
}
