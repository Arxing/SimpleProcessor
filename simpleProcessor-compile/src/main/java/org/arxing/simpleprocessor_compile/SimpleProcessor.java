package org.arxing.simpleprocessor_compile;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.squareup.javapoet.ClassName;
import org.arxing.simpleprocessor_compile.helper.JavaFileHelper;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

@SuppressWarnings("all")
public abstract class SimpleProcessor extends AbstractProcessor {
    private Messager messager;
    private Elements elementUtils;
    private Filer filerUtils;
    private Locale localeUtils;
    private Types typeUtils;
    private Logger logger;
    private ProcessorUtils utils;
    private Set<Class<? extends Annotation>> types;
    private JavaFileHelper fileProxy;

    @Override public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager = processingEnv.getMessager();
        logger = new Logger(messager);
        elementUtils = processingEnv.getElementUtils();
        filerUtils = processingEnv.getFiler();
        localeUtils = processingEnv.getLocale();
        typeUtils = processingEnv.getTypeUtils();
        utils = ProcessorUtils.init(logger, elementUtils, filerUtils, localeUtils, typeUtils);
        onInit();
    }

    @Override public Set<String> getSupportedAnnotationTypes() {
        types = new LinkedHashSet<>();
        types.addAll(defineSupportedTypes());
        return Stream.of(types).map(type -> type.getCanonicalName()).collect(Collectors.toSet());
    }

    @Override public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        //每份文件對應數個註解
        //每個註解對應多個元素
        Map<String, Map<Annotation, Set<ElementBundle>>> data = new HashMap<>();

        for (Class<? extends Annotation> type : types) {
            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(type);
            for (Element element : elements) {
                //過濾Field
                if (!element.getKind().isField())
                    continue;
                Annotation annotation = element.getAnnotation(type);
                VariableElement elField = (VariableElement) element;
                TypeElement elHost = (TypeElement) elField.getEnclosingElement();
                String host = elHost.getQualifiedName().toString();

                if (!data.containsKey(host)) {
                    data.put(host, new HashMap<>());
                }
                Map<Annotation, Set<ElementBundle>> annoMap = data.get(host);
                if (!annoMap.containsKey(annotation)) {
                    annoMap.put(annotation, new LinkedHashSet<>());
                }
                Set<ElementBundle> bundles = annoMap.get(annotation);
                ElementBundle bundle = new ElementBundle(elField, elHost, host, annotation);
                bundles.add(bundle);
            }
        }
        if (simpleMode()) {
            data.forEach((host, annotationMap) -> {
                JavaFileHelper fileHelper = JavaFileHelper.buildClass(guessPackage(host), guessFileName(host));
                fileHelper.addModifiers(Modifier.PUBLIC);
                annotationMap.forEach((annotation, bundles) -> {
                    AnnotationHandler handler = getHandler(annotation, host, fileHelper);
                    if (handler != null) {
                        bundles.forEach(bundle -> {
                            handler.putElement(bundle.elField, bundle.elHost);
                        });
                    } else
                        logger.printWarning("未定義%s的handler", annotation.toString());
                });
                fileHelper.create(filerUtils);
            });
            return true;
        } else {
            return handleProcess(data);
        }
    }

    @Override public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    public void onInit() {
    }

    public abstract Set<Class<? extends Annotation>> defineSupportedTypes();

    public boolean handleProcess(Map<String, Map<Annotation, Set<ElementBundle>>> data) {
        return false;
    }

    public abstract String fileNameSuffix();

    public abstract AnnotationHandler getHandler(Annotation annotation, String host, JavaFileHelper fileHelper);

    public boolean simpleMode() {
        return true;
    }

    public String guessPackage(String host) {
        return ClassName.bestGuess(host).packageName();
    }

    public String guessClass(String host) {
        return ClassName.bestGuess(host).simpleName();
    }

    public String guessFileName(String host) {
        return guessClass(host) + "_" + fileNameSuffix();
    }

}
