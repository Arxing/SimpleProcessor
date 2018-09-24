package org.arxing.demo_compile;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import org.arxing.demo_annotation.Bind;
import org.arxing.simpleprocessor_compile.AnnotationHandler;
import org.arxing.simpleprocessor_compile.ElementBundle;
import org.arxing.simpleprocessor_compile.ProcessorUtils;
import org.arxing.simpleprocessor_compile.SimpleProcessor;
import org.arxing.simpleprocessor_compile.helper.JavaFileHelper;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Processor;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

@AutoService(Processor.class)
public class BindViewProcessor extends SimpleProcessor {
    boolean isWrited = false;

    @Override public Set<Class<? extends Annotation>> defineSupportedTypes() {
        return new HashSet<>(Arrays.asList(Bind.class));
    }

    @Override public String fileNameSuffix() {
        return "_BB";
    }

    @Override public AnnotationHandler getHandler(Annotation annotation, String host, JavaFileHelper fileHelper) {
        return new AnnotationHandler<Bind>(annotation, host) {
            @Override public void putElement(VariableElement elField, TypeElement elHost, JavaFileHelper fileHelper) {

            }
        };
    }

    @Override public boolean simpleMode() {
        return false;
    }

    @Override public boolean handleProcess(Map<String, Map<Annotation, Set<ElementBundle>>> data) {
        if (isWrited)
            return true;
        isWrited = true;

        String host = "org.arxing.simpleProcessorDemo.MainActivity";
        Element el = data.get(host).values().stream().findFirst().get().stream().findFirst().get().elHost;

        try {
            ClassName cn = ClassName.get("org.hh", "Paper", "a");
            TypeSpec helloWorld = TypeSpec.enumBuilder("Roshambo")
                                          .addModifiers(Modifier.PUBLIC)
                                          .addEnumConstant("ROCK",
                                                           TypeSpec.anonymousClassBuilder("$S", "fist")
                                                                   .addMethod(MethodSpec.methodBuilder("toString")
                                                                                        .addAnnotation(Override.class)
                                                                                        .addModifiers(Modifier.PUBLIC)
                                                                                        .addStatement("$T a = new $T();", cn, cn)
                                                                                        .addStatement("return $S", "avalanche!")
                                                                                        .returns(String.class)
                                                                                        .build())
                                                                   .build())
                                          .addEnumConstant("SCISSORS", TypeSpec.anonymousClassBuilder("$S", "peace").build())
                                          .addEnumConstant("PAPER", TypeSpec.anonymousClassBuilder("$S", "flat").build())
                                          .addField(String.class, "handsign", Modifier.PRIVATE, Modifier.FINAL)
                                          .addMethod(MethodSpec.constructorBuilder()
                                                               .addParameter(String.class, "handsign")
                                                               .addStatement("this.$N = $N", "handsign", "handsign")
                                                               .build())
                                          .build();
            JavaFile.builder("com.aa.bb", helloWorld).build().writeTo(ProcessorUtils.getInstance().filerUtils);

            //            JavaFile.builder("com.aa.bb", TypeSpec.enumBuilder("GG")
            //                                                  .addEnumConstant("AA")
            //                                                  .addEnumConstant("BB")
            //                                                  .addEnumConstant("CC")
            //                                                  .addEnumConstant("DD",
            //                                                                   TypeSpec.anonymousClassBuilder("$S", "ff").build())
            //
            //
            //
            //                                                  .build())
            //                    .build()
            //                    .writeTo(ProcessorUtils.getInstance().filerUtils);
        } catch (IOException e) {
            e.printStackTrace();
        }


        //        TypeClassHelper classHelper = TypeHelper.buildClass("GGClass");
        //        classHelper.addModifiers(Modifier.PUBLIC).superClass("android.app.Activity")
        //                   .addTypeVariable(TypeVariableName.get("T1", String.class, Integer.class));
        //
        //        FileHelper fileHelper = FileHelper.build("com.gg.pkg", classHelper);
        //
        //        try {
        //            fileHelper.create().writeTo(ProcessorUtils.getInstance().filerUtils);
        //        } catch (IOException e) {
        //            e.printStackTrace();
        //        }

        return true;
    }
}
