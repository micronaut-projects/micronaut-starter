package io.micronaut.starter.api

trait ConfigurationFixture {

    Map<String, Object> getConfiguration() {
        Map<String, Object> m = [:]
        if (specName) {
            m['spec.name'] = specName
        }
        m
    }

    String getSpecName() {
        null
    }
}
