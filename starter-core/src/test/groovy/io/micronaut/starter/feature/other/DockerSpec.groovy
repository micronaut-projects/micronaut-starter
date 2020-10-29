package io.micronaut.starter.feature.other

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options

class DockerSpec extends BeanContextSpec {

    void "test docker is applied by default for Maven"() {
        when:
        GeneratorContext ctx = buildGeneratorContext([], new Options(Language.JAVA, null, build), applicationType)

        then:
        (build == BuildTool.MAVEN && ctx.templates.containsKey("dockerfile"))||     (build.isGradle() && !ctx.templates.containsKey("dockerfile"))


        where:
        [build, applicationType] << [BuildTool.values(), ApplicationType.values() - ApplicationType.FUNCTION].combinations()
    }
}
