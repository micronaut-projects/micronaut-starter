package io.micronaut.starter.fixture

import io.micronaut.projectgen.core.generator.Project
import io.micronaut.projectgen.core.utils.NameUtils

trait ProjectFixture {

    Project buildProject(String name = 'example.micronaut.foo') {
        NameUtils.parse(name)
    }

}