package io.micronaut.starter.rocker;

import com.fizzed.rocker.model.PostProcessorException;
import com.fizzed.rocker.model.TemplateModel;
import com.fizzed.rocker.model.TemplateModelPostProcessor;

public class WhitespaceProcessor implements TemplateModelPostProcessor {

    @Override
    public TemplateModel process(TemplateModel templateModel, int ppIndex) throws PostProcessorException {
        System.out.println("HEY GOT HERE");
        return templateModel;
    }
}