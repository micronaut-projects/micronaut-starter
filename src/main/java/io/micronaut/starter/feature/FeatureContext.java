package io.micronaut.starter.feature;

import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Predicate;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

public class FeatureContext {

    private final Language language;
    private TestFramework testFramework;
    private final BuildTool buildTool;
    private final List<Feature> features = new ArrayList<>();
    private List<Predicate<Feature>> exclusions = new ArrayList<>();
    private ListIterator<Feature> iterator;

    public FeatureContext(Language language, TestFramework testFramework, BuildTool buildTool) {
        if (language == null) {
            language = Language.java;
        }
        this.language = language;
        features.add(language.getFeature());

        if (buildTool == null) {
            buildTool = BuildTool.gradle;
        }
        this.buildTool = buildTool;
        features.add(buildTool.getFeature());

        this.testFramework = testFramework;
        if (testFramework != null) {
            features.add(testFramework.getFeature());
        }
    }

    public void processSelectedFeatures(List<Feature> features) {
        this.features.addAll(0, features);
        this.iterator = this.features.listIterator();
        while (iterator.hasNext()) {
            Feature feature = iterator.next();
            System.out.println("processing " + feature.getName());
            feature.processSelectedFeatures(this);
        }
        this.iterator = null;
    }

    public void exclude(Predicate<Feature> exclusion) {
        exclusions.add(exclusion);
    }

    public List<Feature> getFeatures() {
        return features.stream().filter(feature -> {
            for (Predicate<Feature> exclusion: exclusions) {
                if (exclusion.test(feature)) {
                    return false;
                }
            }
            return true;
        }).collect(collectingAndThen(toList(), Collections::unmodifiableList));
    }

    public Language getLanguage() {
        return language;
    }

    public TestFramework getTestFramework() {
        return testFramework;
    }

    public BuildTool getBuildTool() {
        return buildTool;
    }

    public void setTestFramework(TestFramework testFramework) {
        this.testFramework = testFramework;
        addFeature(testFramework.getFeature());
    }

    public boolean hasApplicationFeature() {
        return features.stream().anyMatch(feature -> feature instanceof ApplicationFeature);
    }

    public void addFeature(Feature feature) {
        if (iterator != null) {
            iterator.add(feature);
        } else {
            features.add(feature);
        }
        System.out.println("processing " + feature.getName());
        feature.processSelectedFeatures(this);
    }
}
