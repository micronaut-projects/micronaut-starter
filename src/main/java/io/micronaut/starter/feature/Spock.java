package io.micronaut.starter.feature;

public class Spock implements TestFeature {

    private final Feature source;

    public Spock() {
        this.source = null;
    }

    public Spock(Feature source) {
        this.source = source;
    }

    @Override
    public String getName() {
        return "spock";
    }

}
