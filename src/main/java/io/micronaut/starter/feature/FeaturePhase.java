package io.micronaut.starter.feature;

public enum FeaturePhase {

    LOWEST(-200),
    LOW(-100),
    DEFAULT(0),
    LANGUAGE(300),
    TEST(400),
    BUILD(500),
    HIGH(600),
    HIGHEST(700);

    public int getOrder() {
        return order;
    }

    private final int order;

    FeaturePhase(int order) {
        this.order = order;
    }

}
