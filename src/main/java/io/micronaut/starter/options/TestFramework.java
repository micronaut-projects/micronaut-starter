package io.micronaut.starter.options;

import io.micronaut.starter.feature.test.Junit;
import io.micronaut.starter.feature.test.KotlinTest;
import io.micronaut.starter.feature.test.Spock;
import io.micronaut.starter.feature.TestFeature;

public enum TestFramework {

    junit(new Junit()),
    spock(new Spock()),
    kotlintest(new KotlinTest());

    private final TestFeature testFeature;

    TestFramework(TestFeature testFeature) {
        this.testFeature = testFeature;
    }

    public TestFeature getFeature() {
        return testFeature;
    }

    public boolean isJunit() {
        return this == TestFramework.junit;
    }

    public boolean isSpock() {
        return this == TestFramework.spock;
    }

    public boolean isKotlinTest() {
        return this == TestFramework.kotlintest;
    }
}
