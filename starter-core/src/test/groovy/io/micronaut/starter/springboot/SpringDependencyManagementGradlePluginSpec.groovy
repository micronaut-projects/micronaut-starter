package io.micronaut.starter.springboot

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.fixture.CommandOutputFixture
import spock.lang.Shared
import spock.lang.Subject

class SpringDependencyManagementGradlePluginSpec extends BeanContextSpec implements CommandOutputFixture {

    @Shared
    @Subject
    SpringDependencyManagementGradlePlugin feature = beanContext.getBean(SpringDependencyManagementGradlePlugin)

    void "spring-dependency-management-gradle-plugin is not visible"() {
        expect:
        !feature.isVisible()
    }
}
