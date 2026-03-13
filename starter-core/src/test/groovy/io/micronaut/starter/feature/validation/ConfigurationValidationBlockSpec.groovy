package io.micronaut.starter.feature.validation

import spock.lang.Specification

class ConfigurationValidationBlockSpec extends Specification {

    void "toXml renders expected xml structure"() {
        given:
        ConfigurationValidationBlock configurationValidation = ConfigurationValidationBlock.builder()
                .enabled(true)
                .suppressions([".*\\.password"])
                .suppressInjectErrors(["com.example.internal.*"])
                .failOnNotPresent()
                .deduceEnvironments(false)
                .validateDependencyInjection(false)
                .format("both")
                .outputDirectory('${project.build.directory}/micronaut/config-validation')
                .devEnvironments(["dev"])
                .packageValidationEnvironments(["prod"])
                .cacheEnabled()
                .testEnvironments(["ci"])
                .build()

        when:
        String xml = configurationValidation.toXml()
        String expectedXml = '''\
<configurationValidation>
    <enabled>true</enabled>

    <suppressions>
        <suppression>.*\\.password</suppression>
    </suppressions>
    <suppressInjectErrors>
        <suppressInjectError>com.example.internal.*</suppressInjectError>
    </suppressInjectErrors>
    <failOnNotPresent>true</failOnNotPresent>
    <deduceEnvironments>false</deduceEnvironments>
    <validateDependencyInjection>false</validateDependencyInjection>
    <format>both</format>
    <outputDirectory>${project.build.directory}/micronaut/config-validation</outputDirectory>
    <cacheEnabled>true</cacheEnabled>

    <dev>
        <environments>
            <environment>dev</environment>
        </environments>
    </dev>
    <packageValidation>
        <environments>
            <environment>prod</environment>
        </environments>
    </packageValidation>
    <test>
        <environments>
            <environment>ci</environment>
        </environments>
    </test>
</configurationValidation>'''

        then:
        xml == expectedXml
    }

    void "toXml escapes xml reserved characters"() {
        given:
        ConfigurationValidationBlock configurationValidation = ConfigurationValidationBlock.builder()
                .enabled(true)
                .format("bo<th&\"'")
                .outputDirectory("dir<&>")
                .devEnvironments(["d<e&v"])
                .packageValidationEnvironments(["prod\"'"])
                .testEnvironments(["ci"])
                .build()

        when:
        String xml = configurationValidation.toXml()

        then:
        xml.contains("<format>bo&lt;th&amp;&quot;&apos;</format>")
        xml.contains("<outputDirectory>dir&lt;&amp;&gt;</outputDirectory>")
        xml.contains("<environment>d&lt;e&amp;v</environment>")
        xml.contains("<environment>prod&quot;&apos;</environment>")
    }

    void "toXml handles all-null configuration"() {
        given:
        ConfigurationValidationBlock configurationValidation = ConfigurationValidationBlock.builder().build()

        when:
        String xml = configurationValidation.toXml()

        then:
        xml == '''\
<configurationValidation>
</configurationValidation>'''
    }
}
