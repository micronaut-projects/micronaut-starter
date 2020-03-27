package io.micronaut.starter.options;

import io.micronaut.starter.feature.lang.groovy.Groovy;
import io.micronaut.starter.feature.lang.java.Java;
import io.micronaut.starter.feature.lang.kotlin.Kotlin;
import io.micronaut.starter.feature.LanguageFeature;

public enum Language {

    java(new Java(), "annotationProcessor"),
    groovy(new Groovy(), "compileOnly"),
    kotlin(new Kotlin(), "kapt");

    private final LanguageFeature feature;
    private final String gradleScope;

    Language(LanguageFeature feature, String gradleScope) {
        this.feature = feature;
        this.gradleScope = gradleScope;
    }

    public final boolean isJava() {
        return this == Language.java;
    }
    public final boolean isGroovy() {
        return this == Language.groovy;
    }
    public final boolean isKotlin() {
        return this == Language.kotlin;
    }

    public final LanguageFeature getFeature() {
        return feature;
    }

    public String getGradleScope() {
        return gradleScope;
    }
}
