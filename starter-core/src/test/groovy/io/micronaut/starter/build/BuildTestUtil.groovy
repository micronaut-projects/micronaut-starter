/*
 * Copyright 2017-2023 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.starter.build

import groovy.transform.CompileStatic
import io.micronaut.starter.build.gradle.GradleBuildTestVerifier
import io.micronaut.starter.build.maven.MavenBuildTestVerifier
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework

@CompileStatic
class BuildTestUtil {

    static BuildTestVerifier verifier(BuildTool buildTool,
                                      Language language,
                                      TestFramework testFramework,
                                      String template) {
        buildTool.isGradle() ? new GradleBuildTestVerifier(template, language, testFramework) : new MavenBuildTestVerifier(template, language)
    }

    static BuildTestVerifier verifier(BuildTool buildTool,
                                      Language language,
                                      String template) {
        verifier(buildTool, language, language.getDefaults().getTest(), template)
    }

    static BuildTestVerifier verifier(BuildTool buildTool,
                                      String template) {
        Language language = Language.DEFAULT_OPTION
        verifier(buildTool, language, template)
    }
}
