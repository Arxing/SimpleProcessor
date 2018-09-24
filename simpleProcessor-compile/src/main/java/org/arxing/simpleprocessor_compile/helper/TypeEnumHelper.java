package org.arxing.simpleprocessor_compile.helper;

import com.annimon.stream.Stream;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.lang.reflect.Type;

import javax.lang.model.element.Modifier;

public class TypeEnumHelper extends TypeHelper<TypeEnumHelper> {

    TypeEnumHelper(TypeSpec.Kind kind, String name) {
        super(kind, name);
    }

    public TypeEnumHelper addFields(Iterable<FieldSpec> fields) {
        Stream.of(fields).forEach(f -> addField(f));
        return this;
    }

    public TypeEnumHelper addField(FieldSpec field) {
        builder.addField(field);
        return this;
    }

    public TypeEnumHelper addField(Type type, String name, Modifier... modifiers) {
        return addField(FieldSpec.builder(type, name, modifiers).build());
    }

    public TypeEnumHelper addField(TypeName type, String name, Modifier... modifiers) {
        return addField(FieldSpec.builder(type, name, modifiers).build());
    }

    public TypeEnumHelper addMethod(Iterable<MethodSpec> methods) {
        Stream.of(methods).forEach(m -> addMethod(m));
        return this;
    }

    public TypeEnumHelper addMethod(MethodSpec method) {
        builder.addMethod(method);
        return this;
    }

    public TypeEnumHelper addMethod(MethodHelper method) {
        return addMethod(method.create());
    }

}
