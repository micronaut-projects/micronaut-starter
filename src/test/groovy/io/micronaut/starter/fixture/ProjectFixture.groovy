package io.micronaut.starter.fixture

import io.micronaut.starter.Project
import io.micronaut.starter.util.NameUtils

trait ProjectFixture {

    Project buildProject(String name = 'example.micronaut.foo') {
        NameUtils.parse(name)
    }

}