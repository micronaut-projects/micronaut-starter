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
        String output = baos.toString()

        then:
        output.contains("a.b.c=1")
        output.contains("a.x=2")
        output.contains("a.y=3")
        output.contains("a.z[0]=4")
        output.contains("a.z[1]=5")
        output.contains("a.z[2]=6")
        output.contains("d[0].name=Sally")
        output.contains("d[0].age=30")
        output.contains("d[1].name=John")
        output.contains("d[1].age=40")
        output.contains("d[1].petNames[0]=Rover")
    }
}
