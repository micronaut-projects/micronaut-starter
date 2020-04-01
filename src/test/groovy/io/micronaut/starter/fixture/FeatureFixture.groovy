package io.micronaut.starter.fixture

import io.micronaut.starter.feature.Feature
import io.micronaut.starter.feature.Features
import io.micronaut.starter.feature.lang.groovy.Groovy
import io.micronaut.starter.feature.lang.groovy.GroovyApplication
import io.micronaut.starter.feature.lang.java.Java
import io.micronaut.starter.feature.lang.java.JavaApplication
import io.micronaut.starter.feature.lang.kotlin.Kotlin
import io.micronaut.starter.feature.lang.kotlin.KotlinApplication
import io.micronaut.starter.feature.test.Junit
import io.micronaut.starter.feature.test.KotlinTest
import io.micronaut.starter.feature.test.Spock

trait FeatureFixture {

    Java buildJavaFeature() {
        new Java(new JavaApplication(), new Junit())
    }

    Groovy buildGroovyFeature() {
        new Groovy(new GroovyApplication(), new Spock())
    }

    Kotlin buildKotlinFeature() {
        new Kotlin(new KotlinApplication(), new KotlinTest())
    }

    Features buildJavaWithFeatures(Feature ...features) {
        new Features([buildJavaFeature(), new Junit()] + features.toList())
    }

    Features buildKotlinWithFeatures(Feature ...features) {
        new Features([buildKotlinFeature(), new KotlinTest()] + features.toList())
    }

    Features buildGroovyWithFeatures(Feature ...features) {
        new Features([buildGroovyFeature(), new Spock()] + features.toList())
    }

}