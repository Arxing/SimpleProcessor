package org.arxing.simpleprocessor_compile;

import java.lang.annotation.Annotation;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

public abstract class AnnotationHandler<T extends Annotation> {
    protected T annotation;
    protected String hostKey;

    public AnnotationHandler(Annotation annotation, String hostKey) {
        this.annotation = (T) annotation;
        this.hostKey = hostKey;
    }

    public abstract void putElement(VariableElement elField, TypeElement elHost);
}
