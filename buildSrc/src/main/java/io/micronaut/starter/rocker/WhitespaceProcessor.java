package io.micronaut.starter.rocker;

import com.fizzed.rocker.model.PlainText;
import com.fizzed.rocker.model.PostProcessorException;
import com.fizzed.rocker.model.TemplateModel;
import com.fizzed.rocker.model.TemplateModelPostProcessor;
import com.fizzed.rocker.model.TemplateUnit;

import java.util.List;

public class WhitespaceProcessor implements TemplateModelPostProcessor {

    @Override
    public TemplateModel process(TemplateModel templateModel, int ppIndex) throws PostProcessorException {
        List<TemplateUnit> units = templateModel.getUnits();
        int length = units.size();
        PlainText lastPlainText = null;
        for (int i = 0; i < length; i ++) {
            TemplateUnit tu = units.get(i);
            if (tu instanceof PlainText) {
                PlainText pt = (PlainText)tu;
                if ((lastPlainText == null || lastPlainText.getText().endsWith("\n")) && pt.getText().startsWith("\n")) {
                    PlainText replacementPt = new PlainText(pt.getSourceRef(), pt.getText().substring(1));
                    // replace the unit
                    units.add(i, replacementPt);
                    units.remove(i + 1);
                }
                lastPlainText = pt;
            }
        }
        return templateModel;
    }
}
