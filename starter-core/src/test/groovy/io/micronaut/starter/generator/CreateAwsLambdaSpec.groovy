package io.micronaut.starter.generator

import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import spock.lang.Unroll

class CreateAwsLambdaSpec extends CommandSpec {

    @Unroll
    void 'create-#applicationType with features #features #lang and #build and test framework: #testFramework'(ApplicationType applicationType,
                                                                                                                List<String> features,
                                                                                                                Language lang,
                                                                                                                BuildTool build,
                                                                                                                TestFramework testFramework) {
        given:
        generateProject(lang, build, features, applicationType, testFramework)

        when:
        build == BuildTool.GRADLE ? executeGradleCommand('test') : executeMavenCommand("test")

        then:
        testOutputContains("BUILD SUCCESS")

        where:
        applicationType           | features                                     | lang                    | build              | testFramework
        ApplicationType.DEFAULT   | ['aws-lambda']                               | Language.JAVA           | BuildTool.GRADLE   | TestFramework.JUNIT
        ApplicationType.DEFAULT   | ['aws-lambda']                               | Language.JAVA           | BuildTool.MAVEN    | TestFramework.JUNIT
        ApplicationType.DEFAULT   | ['aws-lambda']                               | Language.JAVA           | BuildTool.GRADLE   | TestFramework.SPOCK
        ApplicationType.DEFAULT   | ['aws-lambda']                               | Language.JAVA           | BuildTool.MAVEN    | TestFramework.SPOCK
        ApplicationType.DEFAULT   | ['aws-lambda']                               | Language.KOTLIN         | BuildTool.GRADLE   | TestFramework.KOTLINTEST
        ApplicationType.DEFAULT   | ['aws-lambda']                               | Language.KOTLIN         | BuildTool.MAVEN    | TestFramework.KOTLINTEST
        ApplicationType.DEFAULT   | ['aws-lambda']                               | Language.KOTLIN         | BuildTool.GRADLE   | TestFramework.JUNIT
        ApplicationType.DEFAULT   | ['aws-lambda']                               | Language.KOTLIN         | BuildTool.MAVEN    | TestFramework.JUNIT
        ApplicationType.DEFAULT   | ['aws-lambda']                               | Language.GROOVY         | BuildTool.GRADLE   | TestFramework.SPOCK
        ApplicationType.DEFAULT   | ['aws-lambda']                               | Language.GROOVY         | BuildTool.MAVEN    | TestFramework.SPOCK
        ApplicationType.FUNCTION  | ['aws-lambda']                               | Language.JAVA           | BuildTool.GRADLE   | TestFramework.JUNIT
        ApplicationType.FUNCTION  | ['aws-lambda']                               | Language.JAVA           | BuildTool.MAVEN    | TestFramework.JUNIT
        ApplicationType.FUNCTION  | ['aws-lambda']                               | Language.JAVA           | BuildTool.GRADLE   | TestFramework.SPOCK
        ApplicationType.FUNCTION  | ['aws-lambda']                               | Language.JAVA           | BuildTool.MAVEN    | TestFramework.SPOCK
        ApplicationType.FUNCTION  | ['aws-lambda']                               | Language.KOTLIN         | BuildTool.GRADLE   | TestFramework.KOTLINTEST
        ApplicationType.FUNCTION  | ['aws-lambda']                               | Language.KOTLIN         | BuildTool.MAVEN    | TestFramework.KOTLINTEST
        ApplicationType.FUNCTION  | ['aws-lambda']                               | Language.KOTLIN         | BuildTool.GRADLE   | TestFramework.JUNIT
        ApplicationType.FUNCTION  | ['aws-lambda']                               | Language.KOTLIN         | BuildTool.MAVEN    | TestFramework.JUNIT
        ApplicationType.FUNCTION  | ['aws-lambda']                               | Language.GROOVY         | BuildTool.GRADLE   | TestFramework.SPOCK
        ApplicationType.FUNCTION  | ['aws-lambda']                               | Language.GROOVY         | BuildTool.MAVEN    | TestFramework.SPOCK
        ApplicationType.DEFAULT   | ['aws-lambda', 'graalvm']                    | Language.JAVA           | BuildTool.GRADLE   | TestFramework.JUNIT
        ApplicationType.DEFAULT   | ['aws-lambda', 'graalvm']                    | Language.JAVA           | BuildTool.MAVEN    | TestFramework.JUNIT
        ApplicationType.DEFAULT   | ['aws-lambda', 'graalvm']                    | Language.JAVA           | BuildTool.GRADLE   | TestFramework.SPOCK
        ApplicationType.DEFAULT   | ['aws-lambda', 'graalvm']                    | Language.JAVA           | BuildTool.MAVEN    | TestFramework.SPOCK
        ApplicationType.DEFAULT   | ['aws-lambda', 'graalvm']                    | Language.KOTLIN         | BuildTool.GRADLE   | TestFramework.KOTLINTEST
        ApplicationType.DEFAULT   | ['aws-lambda', 'graalvm']                    | Language.KOTLIN         | BuildTool.MAVEN    | TestFramework.KOTLINTEST
        ApplicationType.DEFAULT   | ['aws-lambda', 'graalvm']                    | Language.KOTLIN         | BuildTool.GRADLE   | TestFramework.JUNIT
        ApplicationType.DEFAULT   | ['aws-lambda', 'graalvm']                    | Language.KOTLIN         | BuildTool.MAVEN    | TestFramework.JUNIT
        ApplicationType.DEFAULT   | ['aws-lambda', 'graalvm']                    | Language.GROOVY         | BuildTool.GRADLE   | TestFramework.SPOCK
        ApplicationType.DEFAULT   | ['aws-lambda', 'graalvm']                    | Language.GROOVY         | BuildTool.MAVEN    | TestFramework.SPOCK
        ApplicationType.FUNCTION  | ['aws-lambda', 'graalvm']                    | Language.JAVA           | BuildTool.GRADLE   | TestFramework.JUNIT
        ApplicationType.FUNCTION  | ['aws-lambda', 'graalvm']                    | Language.JAVA           | BuildTool.MAVEN    | TestFramework.JUNIT
        ApplicationType.FUNCTION  | ['aws-lambda', 'graalvm']                    | Language.JAVA           | BuildTool.GRADLE   | TestFramework.SPOCK
        ApplicationType.FUNCTION  | ['aws-lambda', 'graalvm']                    | Language.JAVA           | BuildTool.MAVEN    | TestFramework.SPOCK
        ApplicationType.FUNCTION  | ['aws-lambda', 'graalvm']                    | Language.KOTLIN         | BuildTool.GRADLE   | TestFramework.KOTLINTEST
        ApplicationType.FUNCTION  | ['aws-lambda', 'graalvm']                    | Language.KOTLIN         | BuildTool.MAVEN    | TestFramework.KOTLINTEST
        ApplicationType.FUNCTION  | ['aws-lambda', 'graalvm']                    | Language.KOTLIN         | BuildTool.GRADLE   | TestFramework.JUNIT
        ApplicationType.FUNCTION  | ['aws-lambda', 'graalvm']                    | Language.KOTLIN         | BuildTool.MAVEN    | TestFramework.JUNIT
        ApplicationType.FUNCTION  | ['aws-lambda', 'graalvm']                    | Language.GROOVY         | BuildTool.GRADLE   | TestFramework.SPOCK
        ApplicationType.FUNCTION  | ['aws-lambda', 'graalvm']                    | Language.GROOVY         | BuildTool.MAVEN    | TestFramework.SPOCK
        ApplicationType.DEFAULT   | ['aws-lambda', 'aws-lambda-custom-runtime']  | Language.JAVA           | BuildTool.GRADLE   | TestFramework.JUNIT
        ApplicationType.DEFAULT   | ['aws-lambda', 'aws-lambda-custom-runtime']  | Language.JAVA           | BuildTool.MAVEN    | TestFramework.JUNIT
        ApplicationType.DEFAULT   | ['aws-lambda', 'aws-lambda-custom-runtime']  | Language.JAVA           | BuildTool.GRADLE   | TestFramework.SPOCK
        ApplicationType.DEFAULT   | ['aws-lambda', 'aws-lambda-custom-runtime']  | Language.JAVA           | BuildTool.MAVEN    | TestFramework.SPOCK
        ApplicationType.DEFAULT   | ['aws-lambda', 'aws-lambda-custom-runtime']  | Language.KOTLIN         | BuildTool.GRADLE   | TestFramework.KOTLINTEST
        ApplicationType.DEFAULT   | ['aws-lambda', 'aws-lambda-custom-runtime']  | Language.KOTLIN         | BuildTool.MAVEN    | TestFramework.KOTLINTEST
        ApplicationType.DEFAULT   | ['aws-lambda', 'aws-lambda-custom-runtime']  | Language.KOTLIN         | BuildTool.GRADLE   | TestFramework.JUNIT
        ApplicationType.DEFAULT   | ['aws-lambda', 'aws-lambda-custom-runtime']  | Language.KOTLIN         | BuildTool.MAVEN    | TestFramework.JUNIT
        ApplicationType.DEFAULT   | ['aws-lambda', 'aws-lambda-custom-runtime']  | Language.GROOVY         | BuildTool.GRADLE   | TestFramework.SPOCK
        ApplicationType.DEFAULT   | ['aws-lambda', 'aws-lambda-custom-runtime']  | Language.GROOVY         | BuildTool.MAVEN    | TestFramework.SPOCK
        ApplicationType.FUNCTION  | ['aws-lambda', 'aws-lambda-custom-runtime']  | Language.JAVA           | BuildTool.GRADLE   | TestFramework.JUNIT
        ApplicationType.FUNCTION  | ['aws-lambda', 'aws-lambda-custom-runtime']  | Language.JAVA           | BuildTool.MAVEN    | TestFramework.JUNIT
        ApplicationType.FUNCTION  | ['aws-lambda', 'aws-lambda-custom-runtime']  | Language.JAVA           | BuildTool.GRADLE   | TestFramework.SPOCK
        ApplicationType.FUNCTION  | ['aws-lambda', 'aws-lambda-custom-runtime']  | Language.JAVA           | BuildTool.MAVEN    | TestFramework.SPOCK
        ApplicationType.FUNCTION  | ['aws-lambda', 'aws-lambda-custom-runtime']  | Language.KOTLIN         | BuildTool.GRADLE   | TestFramework.KOTLINTEST
        ApplicationType.FUNCTION  | ['aws-lambda', 'aws-lambda-custom-runtime']  | Language.KOTLIN         | BuildTool.MAVEN    | TestFramework.KOTLINTEST
        ApplicationType.FUNCTION  | ['aws-lambda', 'aws-lambda-custom-runtime']  | Language.KOTLIN         | BuildTool.GRADLE   | TestFramework.JUNIT
        ApplicationType.FUNCTION  | ['aws-lambda', 'aws-lambda-custom-runtime']  | Language.KOTLIN         | BuildTool.MAVEN    | TestFramework.JUNIT
        ApplicationType.FUNCTION  | ['aws-lambda', 'aws-lambda-custom-runtime']  | Language.GROOVY         | BuildTool.GRADLE   | TestFramework.SPOCK
        ApplicationType.FUNCTION  | ['aws-lambda', 'aws-lambda-custom-runtime']  | Language.GROOVY         | BuildTool.MAVEN    | TestFramework.SPOCK
    }
}
