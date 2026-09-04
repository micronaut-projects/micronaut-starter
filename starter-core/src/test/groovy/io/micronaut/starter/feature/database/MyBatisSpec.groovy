package io.micronaut.starter.feature.database

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.Category
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.BuildToolUtils
import io.micronaut.starter.options.Language
import spock.lang.Shared
import spock.lang.Subject

import static io.micronaut.starter.build.dependencies.MicronautDependencyUtils.GROUP_ID_MICRONAUT_SQL
import static io.micronaut.starter.feature.database.MyBatis.MICRONAUT_MYBATIS_ARTIFACT
import static io.micronaut.starter.feature.database.MyBatis.NAME

class MyBatisSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Shared
    @Subject
    MyBatis myBatis = beanContext.getBean(MyBatis)

    void 'test readme.md with feature mybatis contains links to documentation'() {
        when:
        Map<String, String> output = generate([NAME])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-sql/latest/guide/#mybatis")
        readme.contains("https://mybatis.org/mybatis-3/")
    }

    void "test mybatis belongs to Database category"() {
        expect:
        Category.DATABASE == myBatis.category
    }

    void "test mybatis supports application type #appType"(ApplicationType appType) {
        expect:
        myBatis.supports(appType)

        where:
        appType << ApplicationType.values()
    }

    void "test dependencies are present for buildTool #buildTool"(BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features([NAME])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency(GROUP_ID_MICRONAUT_SQL, MICRONAUT_MYBATIS_ARTIFACT, Scope.COMPILE)

        where:
        buildTool << BuildToolUtils.jvmBuildTools()
    }

    void 'pyronaut rejects mybatis feature'() {
        when:
        new BuildBuilder(beanContext, BuildTool.PYRONAUT)
                .features([NAME])
                .language(Language.PYTHON)
                .render()

        then:
        def e = thrown(IllegalArgumentException)
        e.message == 'Feature mybatis is not supported for Python because it requires Java reflection'
    }
}
