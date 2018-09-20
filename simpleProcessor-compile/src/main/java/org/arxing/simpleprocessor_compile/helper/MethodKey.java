package org.arxing.simpleprocessor_compile.helper;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.squareup.javapoet.TypeName;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

import javax.lang.model.element.Modifier;

public class MethodKey {
    private String[] modifiers;
    private String returnType;
    private String methodName;
    private String[] params;

    private class Compare implements Comparator<String> {

        @Override public int compare(String o1, String o2) {
            return o1.compareTo(o2);
        }
    }

    public static MethodKey from(MethodHelper methodHelper) {
        TypeName[] parameters = methodHelper.parameters == null ? new TypeName[0] : Stream.of(methodHelper.parameters)
                                                                                          .map(param -> param.type)
                                                                                          .collect(Collectors.toList())
                                                                                          .toArray(new TypeName[0]);
        return new MethodKey(methodHelper.modifiers, methodHelper.returnType, methodHelper.methodName, parameters);
    }

    public MethodKey(Modifier[] modifiers, TypeName returnType, String methodName, TypeName[] params) {
        this.modifiers = modifiers == null ? new String[0] : Stream.of(modifiers)
                                                                   .map(Modifier::toString)
                                                                   .sorted(new Compare())
                                                                   .collect(Collectors.toList())
                                                                   .toArray(new String[0]);
        this.returnType = returnType == null ? "" : returnType.toString();
        this.methodName = methodName;
        this.params = params == null ? new String[0] : Stream.of(params)
                                                             .map(TypeName::toString)
                                                             .sorted(new Compare())
                                                             .collect(Collectors.toList())
                                                             .toArray(new String[0]);
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        MethodKey methodKey = (MethodKey) o;
        return Arrays.equals(modifiers, methodKey.modifiers) && Objects.equals(returnType, methodKey.returnType) && Objects.equals(
                methodName,
                methodKey.methodName) && Arrays.equals(params, methodKey.params);
    }

    @Override public int hashCode() {
        int result = Objects.hash(returnType, methodName);
        result = 31 * result + Arrays.hashCode(modifiers);
        result = 31 * result + Arrays.hashCode(params);
        return result;
    }

    @Override public String toString() {
        return "MethodKey{" + "modifiers=" + Arrays.toString(modifiers) + ", returnType='" + returnType + '\'' + ", methodName='" +
                methodName + '\'' + ", params=" + Arrays
                .toString(params) + '}';
    }
}
