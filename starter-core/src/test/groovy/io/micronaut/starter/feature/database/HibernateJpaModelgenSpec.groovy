package io.micronaut.starter.feature.database

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.feature.Category
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Shared

class HibernateJpaModelgenSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Shared
    HibernateJpaModelgen hibernateJpaModelgen = beanContext.getBean(HibernateJpaModelgen)

    void 'test readme.md with feature hibernate-jpamodelgen contains links to docs'() {
        when:
        Map<String, String> output = generate([HibernateJpaModelgen.NAME])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://hibernate.org/orm/tooling/")
        readme.contains("https://micronaut-projects.github.io/micronaut-data/latest/guide/#typeSafeJava")
    }

    void "feature hibernate-jpamodelgen supports applicationType=#applicationType"(ApplicationType applicationType) {
        expect:
        hibernateJpaModelgen.supports(applicationType)

        where:
        applicationType << ApplicationType.values()
    }

    void "feature hibernate-jpamodelgen feature is DATABASE category"() {
        expect:
        hibernateJpaModelgen.category == Category.DATABASE
    }

    void "dependencies are present for #buildTool and #language"(BuildTool buildTool, Language language) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features([HibernateJpaModelgen.NAME])
                .language(language)
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, template)

        then:
        verifier.hasAnnotationProcessor("org.hibernate.orm", "hibernate-jpamodelgen")

        where:
        [buildTool, language] << [BuildTool.values(), Language.values()].combinations()
    }
}
