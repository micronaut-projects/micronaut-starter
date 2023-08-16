package io.micronaut.starter

import io.micronaut.starter.application.Project
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.util.NameUtils
import spock.lang.Issue
import spock.lang.Unroll

class ProjectSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Unroll
    void "test Project.naturalName(#name) == #result project info"() {
        when:
        Project project = NameUtils.parse(name)

        then:
        project.naturalName == result

        where:
        name                           | result
        "raw-tomatoes"                 | "Raw Tomatoes"
        "my-raw-tomatoes"              | "My Raw Tomatoes"
        "aName"                        | "A Name"
        "name"                         | "Name"
        "firstName"                    | "First Name"
        "URL"                          | "URL"
        "localURL"                     | "Local URL"
        "URLLocal"                     | "URL Local"
        "aURLLocal"                    | "A URL Local"
        "MyDomainClass"                | "My Domain Class"
        "com.myco.myapp.MyDomainClass" | "My Domain Class"
    }

    @Unroll
    void "test Project.propertyName(#name) == #result project info"() {
        when:
        Project project = NameUtils.parse(name)

        then:
        project.propertyName == result

        where:
        name                           | result
        "raw-tomatoes"                 | "raw-tomatoes"
        "my-raw-tomatoes"              | "my-raw-tomatoes"
        "aName"                        | "aName"
        "name"                         | "name"
        "firstName"                    | "firstName"
        "URL"                          | "URL"
        "localURL"                     | "localURL"
        "URLLocal"                     | "URLLocal"
        "aURLLocal"                    | "aURLLocal"
        "MyDomainClass"                | "myDomainClass"
        "com.myco.myapp.MyDomainClass" | "myDomainClass"
    }

    @Issue("https://github.com/micronaut-projects/micronaut-starter/issues/1981")
    void 'test project micronaut.application.name configuration'() {
        when:
        Map<String,String> project = generate("raw-tomatoes")
        String config = project.get("src/main/resources/application.properties")

        then:
        config.contains("micronaut.application.name=raw-tomatoes")

        when:
        project = generate("rawTomatoes")
        config = project.get("src/main/resources/application.properties")

        then:
        config.contains("micronaut.application.name=rawTomatoes")
    }
}
