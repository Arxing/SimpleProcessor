package org.arxing.simpleprocessor_compile.helper;

import com.annimon.stream.Stream;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

import java.lang.reflect.Type;
import javax.lang.model.element.Modifier;

@SuppressWarnings("all")
public class TypeClassHelper extends TypeHelper<TypeClassHelper> {

    TypeClassHelper(TypeSpec.Kind kind, String name) {
        super(kind, name);
    }

    public TypeClassHelper addTypeVariables(Iterable<TypeVariableName> typeVariables) {
        builder.addTypeVariables(typeVariables);
        return this;
    }

    public TypeClassHelper addTypeVariable(TypeVariableName typeVariable) {
        builder.addTypeVariable(typeVariable);
        return this;
    }

    public TypeClassHelper superClass(TypeName typeName) {
        builder.superclass(typeName);
        return this;
    }

    public TypeClassHelper superClass(String className) {
        return superClass(ClassName.bestGuess(className));
    }

    public TypeClassHelper superClass(Type type) {
        return superClass(TypeName.get(type));
    }

    public TypeClassHelper addSuperInterfaces(Iterable<? extends TypeName> superinterfaces) {
        builder.addSuperinterfaces(superinterfaces);
        return this;
    }

    public TypeClassHelper addSuperInterface(TypeName superinterface) {
        builder.addSuperinterface(superinterface);
        return this;
    }

    public TypeClassHelper addSuperInterface(Type superinterface) {
        return addSuperInterface(TypeName.get(superinterface));
    }

    public TypeClassHelper addSuperInterface(String superinterface) {
        return addSuperInterface(ClassName.bestGuess(superinterface));
    }

    public TypeClassHelper addFields(Iterable<FieldSpec> fields) {
        Stream.of(fields).forEach(f -> addField(f));
        return this;
    }

    public TypeClassHelper addField(FieldSpec field) {
        builder.addField(field);
        return this;
    }

    public TypeClassHelper addField(Type type, String name, Modifier... modifiers) {
        return addField(FieldSpec.builder(type, name, modifiers).build());
    }

    public TypeClassHelper addField(TypeName type, String name, Modifier... modifiers) {
        return addField(FieldSpec.builder(type, name, modifiers).build());
    }

    public TypeClassHelper addMethod(Iterable<MethodSpec> methods) {
        Stream.of(methods).forEach(m -> addMethod(m));
        return this;
    }

    public TypeClassHelper addMethod(MethodSpec method) {
        builder.addMethod(method);
        return this;
    }

    public TypeClassHelper addMethod(MethodHelper method) {
        return addMethod(method.create());
    }

    public TypeClassHelper addStaticBlock(CodeBlock block) {
        builder.addStaticBlock(block);
        builder.addStaticBlock(CodeBlock.of("\n"));
        return this;
    }

    public TypeClassHelper addStaticBlock(String template, ParamBox params) {
        return addStaticBlock(CodeBlock.of(Utils.renderCode(template, params)));
    }

    public TypeClassHelper addInitializerBlock(CodeBlock block) {
        builder.addInitializerBlock(block);
        builder.addStaticBlock(CodeBlock.of("\n"));
        return this;
    }

    public TypeClassHelper addInitializerBlock(String template, ParamBox params) {
        return addInitializerBlock(CodeBlock.of(Utils.renderCode(template, params)));
    }

    public TypeClassHelper addEnumConstant(String name){
        builder.addEnumConstant(name);
        return this;
    }
}
