package io.micronaut.starter.feature.validation

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.Project
import io.micronaut.starter.util.NameUtils
import spock.lang.Shared
import spock.lang.Unroll

class ProjectValidatorSpec extends BeanContextSpec {

    ProjectNameValidator projectValidator = beanContext.getBean(ProjectNameValidator)

    @Shared
    String packageName

    @Override
    Project buildProject() {
        return new Project(packageName, "", "", "", "", "")
    }

    void "test project validator with invalid package name: example.#name"() {
        when:
        def mockedProject =  NameUtils.parse("example." + name + ".test")
        projectValidator.validate(mockedProject)

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == "Invalid package name: %s".formatted("example." +name)

        where:
        name << ["abstract","assert","boolean","break","byte","case","catch","class","char","const","continue","default","double","do","else","final","goto","for","float","implements","import","instanceof","int","interface","long","native","new","package","private","protected","public","return","static","short","strictfp","static","super","switch","synchronized","this","throw","transient","try","void","volatile","while"]
    }


    void "test project validator with valid package name"() {
        when:
        def mockedProject =  NameUtils.parse('io.micronaut.foo')
        projectValidator.validate(mockedProject)

        then:
        noExceptionThrown()
    }

    @Unroll
    void "test invalid package #name name using generator context" () {
        when:
        packageName = name
        buildGeneratorContext([])

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == "Invalid package name: %s".formatted(name)

        where:
        name << ["abstract","assert","boolean","break","byte","case","catch","class","char","const","continue","default","double","do","else","final","goto","for","float","implements","import","instanceof","int","interface","long","native","new","package","private","protected","public","return","static","short","strictfp","static","super","switch","synchronized","this","throw","transient","try","void","volatile","while"]
    }

    void "test valid package name: #name using generator context" () {
        when:
        packageName = name
        def context = buildGeneratorContext([])

        then:
        noExceptionThrown()
        context.getProject().getPackageName() == "io.micronaut.foo"

        where:
        name << ["io.micronaut.foo", "io.micronaut.foo.", "io.micronaut.foo.."]
    }

}
