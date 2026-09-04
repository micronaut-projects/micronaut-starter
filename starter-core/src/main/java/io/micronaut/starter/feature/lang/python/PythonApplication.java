/*
 * Copyright 2017-2026 original authors
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
package io.micronaut.starter.feature.lang.python;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.StringUtils;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.ApplicationFeature;
import io.micronaut.starter.feature.CodeContributingFeature;
import io.micronaut.starter.feature.FeaturePhase;
import io.micronaut.starter.template.StringTemplate;
import jakarta.inject.Singleton;

@Requires(property = "micronaut.starter.feature.python.application.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class PythonApplication implements ApplicationFeature, CodeContributingFeature {

    @Override
    public String getName() {
        return "python-application";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return applicationType != ApplicationType.CLI && applicationType != ApplicationType.GRPC;
    }

    @Override
    public int getOrder() {
        return FeaturePhase.LANGUAGE.getOrder();
    }

    @Override
    @Nullable
    public String mainClassName(GeneratorContext generatorContext) {
        return null;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addTemplate("pyronautMain", new StringTemplate("src/main.py", main()));
        generatorContext.addTemplate("pyronautApplicationTest", new StringTemplate("tests/test_application.py", test()));
    }

    private static String main() {
        return """
            from logback.config import dictConfig

            LOGGING = {
                "version": 1,
                "disable_existing_loggers": False,
                "formatters": {
                    "colored": {
                        "format": "%cyan(%d{HH:mm:ss.SSS}) %highlight(%-5level) %magenta(%logger{36}): %msg"
                    }
                },
                "handlers": {
                    "console": {
                        "class": "logging.StreamHandler",
                        "level": "INFO",
                        "formatter": "colored",
                        "stream": "ext://sys.stdout"
                    }
                },
                "root": {
                    "level": "INFO",
                    "handlers": ["console"]
                }
            }

            dictConfig(LOGGING)
            """;
    }

    private static String test() {
        return """
            import pytest

            from pyronaut.test import *

            @pytest.fixture
            def my_context(request):
                fixture = micronaut_test_fixture(request, MicronautTest(environments=["test"], transactional=False))
                yield fixture
                fixture.stop()

            def test_context(my_context):
                assert my_context.isRunning()
            """;
    }
}
