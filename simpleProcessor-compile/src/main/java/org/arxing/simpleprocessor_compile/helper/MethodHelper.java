package org.arxing.simpleprocessor_compile.helper;

import com.annimon.stream.Stream;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import org.arxing.simpleprocessor_compile.ProcessorUtils;
import org.stringtemplate.v4.ST;
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
public class MethodHelper {
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

    public void addCode(String template, ParamBox params) {
        ST st = new ST(template);
        Stream.of(params.map).forEach(entry -> st.add(entry.getKey(), entry.getValue()));
        String code = st.render();
        methodBuilder.addCode(code);
        methodBuilder.addCode("\n");
        ProcessorUtils.getInstance().logger.printNote("寫入 %s", code);
    }

    public MethodSpec create() {
        return methodBuilder.build();
    }

    public MethodSpec.Builder createBuilder() {
        return methodBuilder;
    }
}
