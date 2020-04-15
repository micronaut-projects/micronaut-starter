package io.micronaut.starter;

import io.micronaut.context.BeanContext;
import picocli.CommandLine;

import java.util.Optional;

/**
 * Picocli factory implementation that uses a Micronaut BeanContext to obtain bean instances.
 *
 * @author Álvaro Sánchez-Mariscal
 * @since 1.0.0
 */
class MicronautFactory implements CommandLine.IFactory {
    private final BeanContext beanContext;
    CommandLine.IFactory defaultFactory;

    public MicronautFactory() {
        this(BeanContext.run());
    }

    public MicronautFactory(BeanContext beanContext) {
        this.beanContext = beanContext;
        defaultFactory = CommandLine.defaultFactory();
    }

    @Override
    public <K> K create(Class<K> cls) throws Exception {
        Optional<K> bean = beanContext.findOrInstantiateBean(cls);
        if (bean.isPresent()) {
            return bean.get();
        } else {
            return defaultFactory.create(cls);
        }
    }
}
