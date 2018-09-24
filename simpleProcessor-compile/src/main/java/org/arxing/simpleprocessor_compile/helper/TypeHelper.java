package org.arxing.simpleprocessor_compile.helper;

import com.annimon.stream.Stream;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeSpec;

import org.arxing.simpleprocessor_compile.AbstractHelper;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.Modifier;

/*
 *
 * 一個type由幾個資訊組成
 * 1. modifiers
 * 2. 類型 (class|enum|interface|@interface)
 * 3. 名稱
 * 4. 成員 (屬性|方法|內部Type)
 * 6. 註解(comment) //暫不支援
 * 7. 註解(annotation) //暫不支援
 * */
@SuppressWarnings("all")
public class TypeHelper<TSelf> extends AbstractHelper<TypeSpec, TypeSpec.Builder> {
    TypeSpec.Builder builder;
    Set<Modifier> modifiers = new HashSet<>();
    Set<FieldHelper> fields = new HashSet<>();
    Map<MethodKey, MethodHelper> methodMap = new HashMap<>();
    Set<TypeHelper> types = new HashSet<>();
    CodeBlock.Builder javadoc = CodeBlock.builder();

    /*
     * 生成一個Type的最低要求資訊
     * */
    TypeHelper(TypeSpec.Kind kind, String name) {
        switch (kind) {
            case ENUM:
                builder = TypeSpec.enumBuilder(name);
                break;
            case CLASS:
                builder = TypeSpec.classBuilder(name);
                break;
            case INTERFACE:
                builder = TypeSpec.interfaceBuilder(name);
                break;
            case ANNOTATION:
                builder = TypeSpec.annotationBuilder(name);
                break;
        }
    }

    public static TypeClassHelper buildClass(String name) {
        return new TypeClassHelper(TypeSpec.Kind.CLASS, name);
    }

    public static TypeHelper buildEnum(String name) {
        return new TypeHelper(TypeSpec.Kind.ENUM, name);
    }

    public static TypeHelper buildInterface(String name) {
        return new TypeHelper(TypeSpec.Kind.INTERFACE, name);
    }

    public static TypeHelper buildAnnotation(String name) {
        return new TypeHelper(TypeSpec.Kind.ANNOTATION, name);
    }

    @Override public TypeSpec create() {
        builder.addJavadoc(javadoc.build());
        return builder.build();
    }

    public TSelf addModifiers(Modifier... modifiers) {
        builder.addModifiers(modifiers);
        return (TSelf) this;
    }

    public TSelf addJavadoc(CodeBlock block) {
        builder.addJavadoc(block);
        return (TSelf) this;
    }

    public TSelf addJavadoc(String format, Object... args) {
        return addJavadoc(CodeBlock.of(format, args));
    }

    public TSelf addAnnotations(Iterable<AnnotationSpec> annotationSpecs) {
        Stream.of(annotationSpecs).forEach(a -> addAnnotation(a));
        return (TSelf) this;
    }

    public TSelf addAnnotation(AnnotationSpec annotation) {
        builder.addAnnotation(annotation);
        return (TSelf) this;
    }

    public TSelf addAnnotation(Class<? extends Annotation> annotation) {
        return addAnnotation(AnnotationSpec.builder(annotation).build());
    }

    public TSelf addAnnotation(ClassName annotation) {
        return addAnnotation(AnnotationSpec.builder(annotation).build());
    }

    public TSelf addTypes(Iterable<TypeSpec> typeSpecs) {
        Stream.of(typeSpecs).forEach(t -> addType(t));
        return (TSelf) this;
    }

    public TSelf addType(TypeSpec typeSpec) {
        builder.addType(typeSpec);
        return (TSelf) this;
    }

}
