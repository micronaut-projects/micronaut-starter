package io.micronaut.starter.fixture

import io.micronaut.starter.feature.Feature
import io.micronaut.starter.feature.Features
import io.micronaut.starter.feature.lang.LanguageFeature
import io.micronaut.starter.feature.lang.groovy.Groovy
import io.micronaut.starter.feature.lang.groovy.GroovyApplication
import io.micronaut.starter.feature.lang.java.Java
import io.micronaut.starter.feature.lang.java.JavaApplication
import io.micronaut.starter.feature.lang.kotlin.Kotlin
import io.micronaut.starter.feature.lang.kotlin.KotlinApplication
import io.micronaut.starter.feature.micrometer.AppOptics
import io.micronaut.starter.feature.micrometer.Atlas
import io.micronaut.starter.feature.micrometer.AzureMonitor
import io.micronaut.starter.feature.micrometer.CloudWatch
import io.micronaut.starter.feature.micrometer.Core
import io.micronaut.starter.feature.micrometer.DataDog
import io.micronaut.starter.feature.micrometer.Dynatrace
import io.micronaut.starter.feature.micrometer.Elastic
import io.micronaut.starter.feature.micrometer.Ganglia
import io.micronaut.starter.feature.micrometer.Graphite
import io.micronaut.starter.feature.micrometer.Humio
import io.micronaut.starter.feature.micrometer.Influx
import io.micronaut.starter.feature.micrometer.Jmx
import io.micronaut.starter.feature.micrometer.Kairos
import io.micronaut.starter.feature.micrometer.NewRelic
import io.micronaut.starter.feature.micrometer.Prometheus
import io.micronaut.starter.feature.micrometer.Signalfx
import io.micronaut.starter.feature.micrometer.Stackdriver
import io.micronaut.starter.feature.micrometer.Statsd
import io.micronaut.starter.feature.micrometer.Wavefront
import io.micronaut.starter.feature.other.Management
import io.micronaut.starter.feature.test.Junit
import io.micronaut.starter.feature.test.KotlinTest
import io.micronaut.starter.feature.test.Spock
import io.micronaut.starter.feature.test.TestFeature
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework

import java.beans.beancontext.BeanContext

trait FeatureFixture {

    Java buildJavaFeature() {
        new Java([new JavaApplication()], new Junit())
    }

    Groovy buildGroovyFeature() {
        new Groovy([new GroovyApplication()], new Spock())
    }

    Kotlin buildKotlinFeature() {
        new Kotlin([new KotlinApplication()], new KotlinTest())
    }

    Features buildWithFeatures(Language language, TestFramework testFramework, Feature ...features) {
        TestFeature testFeature
        if (testFramework == TestFramework.junit) {
            testFeature = new Junit()
        } else if (testFramework == TestFramework.spock) {
            testFeature = new Spock()
        } else if (testFramework == TestFramework.kotlintest) {
            testFeature = new KotlinTest()
        }
        if (language == Language.java) {
            return build(buildJavaFeature(), testFeature, features)
        } else if (language == Language.groovy) {
            return build(buildGroovyFeature(), testFeature, features)
        } else if (language == Language.kotlin) {
            return build(buildKotlinFeature(), testFeature, features)
        }
    }

    Features buildWithFeatures(Language language, Feature ...features) {
        if (language == Language.java) {
            return buildWithFeatures(language, TestFramework.junit, features)
        }
        if (language == Language.groovy) {
            return buildWithFeatures(language, TestFramework.spock, features)
        }
        if (language == Language.kotlin) {
            return buildWithFeatures(language, TestFramework.kotlintest, features)
        }
    }

    private Features build(LanguageFeature languageFeature, TestFeature testFeature, Feature... features) {
        new Features([languageFeature, testFeature] + features.toList())
    }

}