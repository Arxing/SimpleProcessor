package org.arxing.simpleprocessor_compile;

import java.lang.annotation.Annotation;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

public class ElementBundle {
    VariableElement elField;
    TypeElement elHost;
    String hostKey;
    Annotation annotation;

    public ElementBundle(VariableElement elField, TypeElement elHost, String hostKey, Annotation annotation) {
        this.elField = elField;
        this.elHost = elHost;
        this.hostKey = hostKey;
        this.annotation = annotation;
    }
}
