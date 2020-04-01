package io.micronaut.starter.feature;

import io.micronaut.starter.feature.test.TestFeature;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;

import java.util.*;
import java.util.function.Predicate;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

public class FeatureContext {

    private final Language language;
    private final List<Feature> selectedFeatures;
    private TestFramework testFramework;
    private final BuildTool buildTool;
    private final List<Feature> features = new ArrayList<>();
    private List<Predicate<Feature>> exclusions = new ArrayList<>();
    private ListIterator<Feature> iterator;

    public FeatureContext(Language language,
                          TestFramework testFramework,
                          BuildTool buildTool,
                          AvailableFeatures availableFeatures,
                          List<Feature> selectedFeatures) {
        this.selectedFeatures = selectedFeatures;

        this.language = calculateLanguage(language, selectedFeatures);
        availableFeatures.findFeature(this.language.name(), true).ifPresent(features::add);

        if (buildTool == null) {
            buildTool = BuildTool.gradle;
        }
        this.buildTool = buildTool;
        availableFeatures.findFeature(this.buildTool.name(), true).ifPresent(features::add);

        if (testFramework != null) {
            TestFeature testFeature = availableFeatures.findFeature(testFramework.name(), true)
                    .map(TestFeature.class::cast)
                    .orElseThrow(() -> new IllegalArgumentException(String.format("No test feature found for test framework [%s]", testFramework.name())));
            setTestFramework(testFramework, testFeature);
        }
    }

    public void processSelectedFeatures() {
        this.features.addAll(0, selectedFeatures);
        this.features.sort(Comparator.comparingInt(Feature::getOrder));
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

    public void setTestFramework(TestFramework testFramework, TestFeature testFeature) {
        this.testFramework = testFramework;
        addFeature(testFeature);
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

    private Language calculateLanguage(Language language, List<Feature> selectedFeatures) {
        Map<Language, Set<String>> requiredLanguages = new HashMap<>();
        for (Feature feature: selectedFeatures) {
            feature.getRequiredLanguage().ifPresent(lang -> {
                requiredLanguages.compute(lang, (key, value) -> {
                    if (value == null) {
                        value = new HashSet<>();
                    }
                    value.add(feature.getName());
                    return value;
                });
            });
        }

        Set<Language> languages = requiredLanguages.keySet();
        Iterator<Language> languageIterator = languages.iterator();

        if (languages.size() > 1) {
            Language first = languageIterator.next();
            Language second = languageIterator.next();
            throw new IllegalArgumentException(String.format("The selected features are incompatible. %s requires %s and %s requires %s", requiredLanguages.get(first), first, requiredLanguages.get(second), second));
        }

        Language requiredLanguage = languageIterator.hasNext() ? languageIterator.next() : null;

        if (language == null) {
            language = requiredLanguage != null ? requiredLanguage : Language.java;
        } else {
            if (requiredLanguage != null && requiredLanguage != language) {
                throw new IllegalArgumentException(String.format("The selected features are incompatible. %s requires %s but %s was the selected language.", requiredLanguages.get(requiredLanguage), requiredLanguage, language));
            } else {
                language = requiredLanguage != null ? requiredLanguage : language;
            }
        }
        return language;
    }
}
