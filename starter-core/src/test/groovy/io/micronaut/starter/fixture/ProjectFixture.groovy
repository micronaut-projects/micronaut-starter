package io.micronaut.starter.fixture

import io.micronaut.starter.sdk.Project
import io.micronaut.starter.sdk.utils.NameUtils

trait ProjectFixture {

    Project buildProject(String name = 'example.micronaut.foo') {
        NameUtils.parse(name)
    }

}