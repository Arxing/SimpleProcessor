package org.arxing.simpleprocessor_compile;

import java.util.Locale;

import javax.annotation.processing.Filer;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

public class ProcessorUtils {
    private static ProcessorUtils instance;
    public Logger logger;
    public Elements elementUtils;
    public Filer filerUtils;
    public Locale localeUtils;
    public Types typeUtils;

    private ProcessorUtils(Logger logger, Elements elementUtils, Filer filerUtils, Locale localeUtils, Types typeUtils) {
        this.logger = logger;
        this.elementUtils = elementUtils;
        this.filerUtils = filerUtils;
        this.localeUtils = localeUtils;
        this.typeUtils = typeUtils;
    }

    public static ProcessorUtils init(Logger logger, Elements elementUtils, Filer filerUtils, Locale localeUtils, Types typeUtils) {
        instance = new ProcessorUtils(logger, elementUtils, filerUtils, localeUtils, typeUtils);
        return instance;
    }

    public static ProcessorUtils getInstance() {
        return instance;
    }
}
