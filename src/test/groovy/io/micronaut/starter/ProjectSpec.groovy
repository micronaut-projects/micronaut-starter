package io.micronaut.starter

import io.micronaut.starter.util.NameUtils
import spock.lang.Specification
import spock.lang.Unroll

class ProjectSpec extends Specification {

    @Unroll
    void "test Project.naturalName(#name) == #result project info"() {
        when:
        Project project = NameUtils.parse(name)

        then:
        project.naturalName == result

        where:
        name                           | result
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
}
