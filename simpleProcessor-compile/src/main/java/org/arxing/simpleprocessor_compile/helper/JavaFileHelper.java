package org.arxing.simpleprocessor_compile.helper;

import com.annimon.stream.Stream;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;

/*
 *
 * 一份java文件內容由幾個資訊組成
 * 1. package
 * 2. 檔名(等同class|enum|interface|@interface name)
 * 3. modifier
 * 4. import
 * 5. 屬性, 方法, 巢狀類別
 * 6. 註解(comment)
 * 7. 註解(annotation)
 *
 * */
@SuppressWarnings("all")
public class JavaFileHelper {
    private JavaFile.Builder fileBuilder;
    private TypeSpec.Builder typeBuilder;
    private List<FieldSpec.Builder> fieldBuilderList = new ArrayList<>();

    private String pkg;
    private Modifier[] modifiers;
    private String fileName;
    private TypeSpec.Kind kind;
    private Map<MethodKey, MethodHelper> methodMap = new HashMap<>();

    /**
     * 定義一份java文件生成的最低要求資訊
     *
     * @param pkg       包名
     * @param modifiers 修飾子
     * @param fileName  檔名 類名
     * @param kind      類別|列舉|介面|註解
     */
    private JavaFileHelper(String pkg, String fileName, TypeSpec.Kind kind) {
        this.pkg = pkg;
        this.fileName = fileName;
        this.kind = kind;

        ClassName className = ClassName.bestGuess(pkg.concat(".").concat(fileName));
        switch (kind) {
            case ENUM:
                typeBuilder = TypeSpec.enumBuilder(className);
                break;
            case CLASS:
                typeBuilder = TypeSpec.classBuilder(className);
                break;
            case INTERFACE:
                typeBuilder = TypeSpec.interfaceBuilder(className);
                break;
            case ANNOTATION:
                typeBuilder = TypeSpec.annotationBuilder(className);
                break;
        }
    }

    public static JavaFileHelper buildClass(String pkg, String className) {
        JavaFileHelper helper = new JavaFileHelper(pkg, className, TypeSpec.Kind.CLASS);
        return helper;
    }

    public static JavaFileHelper buildEnum(String pkg, String enumName) {
        JavaFileHelper helper = new JavaFileHelper(pkg, enumName, TypeSpec.Kind.ENUM);
        return helper;
    }

    public static JavaFileHelper buildInterface(String pkg, String interfaceName) {
        JavaFileHelper helper = new JavaFileHelper(pkg, interfaceName, TypeSpec.Kind.INTERFACE);
        return helper;
    }

    public static JavaFileHelper buildAnnotation(String pkg, String annotationName) {
        JavaFileHelper helper = new JavaFileHelper(pkg, annotationName, TypeSpec.Kind.ANNOTATION);
        return helper;
    }

    public JavaFileHelper addModifiers(Modifier... modifiers) {
        typeBuilder.addModifiers(modifiers);
        return this;
    }

    /**
     * 插入一個的方法
     */
    public void addMethod(MethodHelper methodHelper) {
        methodMap.put(MethodKey.from(methodHelper), methodHelper);
    }

    /**
     * 將一個函式連接至一個檔案內
     */
    public void linkMethod(MethodHelper helper) {
        MethodKey key = MethodKey.from(helper);
        if (!methodMap.containsKey(key))
            methodMap.put(key, helper);
        else{
            MethodHelper found = methodMap.get(key);
            helper.methodBuilder = found.methodBuilder;
        }
    }

    /**
     * 生成文件
     */
    public boolean create(Filer filer) {
        Stream.of(methodMap).forEach(entry -> typeBuilder.addMethod(entry.getValue().create()));
        TypeSpec typeSpec = typeBuilder.build();
        fileBuilder = JavaFile.builder(pkg, typeSpec);
        JavaFile file = fileBuilder.build();
        try {
            file.writeTo(filer);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
