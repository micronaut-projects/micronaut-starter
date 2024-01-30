package io.micronaut.starter.test

import groovy.transform.CompileStatic
import org.gradle.internal.os.OperatingSystem

import java.util.function.Predicate

@CompileStatic
final class PredicateUtils {
    private PredicateUtils() {
    }

    static Predicate<String> skipFeatureIfMacOS(List<String> featuresNames) {
        return new Predicate<String>() {
            @Override
            boolean test(String s) {
                if (featuresNames.contains(s)) {
                    return !OperatingSystem.current().isMacOsX()
                }
                true
            }
        }
    }
}
