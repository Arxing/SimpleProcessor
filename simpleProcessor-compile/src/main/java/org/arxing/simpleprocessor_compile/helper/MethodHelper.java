package org.arxing.simpleprocessor_compile.helper;

import com.annimon.stream.Stream;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;

import org.arxing.simpleprocessor_compile.AbstractHelper;
import org.arxing.simpleprocessor_compile.ProcessorUtils;
import org.stringtemplate.v4.ST;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import javax.lang.model.element.Modifier;

/*
 * 一個方法由幾個資訊組成
 * 1. modifier
 * 2. return type
 * 3. method name
 * 4. method parameter
 * 5. annotation
 * 6. codes
 *
 * */
public class MethodHelper extends AbstractHelper<MethodSpec, MethodSpec.Builder> {
   MethodSpec.Builder methodBuilder;

   Modifier[] modifiers;
   TypeName returnType;
   String methodName;
   ParameterSpec[] parameters;

    private MethodHelper(String methodName) {
        this.methodName = methodName;
        methodBuilder = MethodSpec.methodBuilder(methodName);
    }

    public static MethodHelper build(String methodName) {
        return new MethodHelper(methodName);
    }

    public MethodHelper addModifiers(Modifier... modifiers) {
        methodBuilder.addModifiers(modifiers);
        return this;
    }

    public MethodHelper addParameter(TypeName type, String name, Modifier... modifiers) {
        methodBuilder.addParameter(type, name, modifiers);
        return this;
    }

    public MethodHelper addParameter(Type type, String name, Modifier... modifiers) {
        methodBuilder.addParameter(type, name, modifiers);
        return this;
    }

    public MethodHelper addParameter(ParameterSpec spec) {
        methodBuilder.addParameter(spec);
        return this;
    }

    public MethodHelper addParameter(String className, String name, Modifier... modifiers) {
        ClassName cn = ClassName.bestGuess(className);
        return addParameter(cn, name, modifiers);
    }

    public MethodHelper addCode(String template, ParamBox params) {
        methodBuilder.addCode(Utils.renderCode(template, params));
        methodBuilder.addCode("\n");
        return this;
    }

    public MethodHelper returns(TypeName type){
        methodBuilder.returns(type);
        return this;
    }

    public MethodHelper returns(Type type){
        methodBuilder.returns(type);
        return this;
    }

    public MethodHelper addAnnotation(Class<? extends Annotation> annotationType){
        methodBuilder.addAnnotation(annotationType);
        return this;
    }

    public MethodHelper addAnnotation(ClassName annotationType){
        methodBuilder.addAnnotation(annotationType);
        return this;
    }

    public MethodHelper addAnnotation(AnnotationSpec spec){
        methodBuilder.addAnnotation(spec);
        return this;
    }

    public MethodSpec create() {
        return methodBuilder.build();
    }

    public MethodSpec.Builder createBuilder() {
        return methodBuilder;
    }
}
