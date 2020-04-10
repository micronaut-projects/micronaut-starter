package io.micronaut.starter.template

import spock.lang.Specification

class PropertiesTemplateSpec extends Specification {

    void "test properties template"() {
        when:
        Map<String, Object> config = [
                "a.b.c": 1,
                "a": [
                        "x": 2,
                        "y": 3,
                        "z": [4,5,6]
                ],
                "d": [
                        [name: "Sally", age: 30],
                        [name: "John", age: 40, petNames: ["Rover"]],
                ]
        ]
        PropertiesTemplate template = new PropertiesTemplate(null, config)
        ByteArrayOutputStream baos = new ByteArrayOutputStream()
        template.write(baos)

        then:
        baos.toString().contains("""
a.b.c=1
a.x=2
a.y=3
a.z[0]=4
a.z[1]=5
a.z[2]=6
d[0].name=Sally
d[0].age=30
d[1].name=John
d[1].age=40
d[1].petNames[0]=Rover
""")
    }
}
