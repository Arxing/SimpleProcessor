package org.arxing.simpleprocessor_compile;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;

public class Logger {
    private Messager messager;

    public Logger(Messager messager) {
        this.messager = messager;
    }

    public void printNote(String format, Object... objs) {
        print(Diagnostic.Kind.NOTE, format, objs);
    }

    public void printError(String format, Object... objs) {
        print(Diagnostic.Kind.ERROR, format, objs);
    }

    public void printMandatoryWarning(String format, Object... objs) {
        print(Diagnostic.Kind.MANDATORY_WARNING, format, objs);
    }

    public void printOther(String format, Object... objs) {
        print(Diagnostic.Kind.OTHER, format, objs);
    }

    public void printWarning(String format, Object... objs) {
        print(Diagnostic.Kind.WARNING, format, objs);
    }

    private void print(Diagnostic.Kind kind, String format, Object... objs) {
        messager.printMessage(kind, String.format(format, objs));
    }
}
