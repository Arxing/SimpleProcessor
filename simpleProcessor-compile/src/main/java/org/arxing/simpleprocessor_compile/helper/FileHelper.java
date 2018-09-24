package org.arxing.simpleprocessor_compile.helper;

import com.squareup.javapoet.JavaFile;

import org.arxing.simpleprocessor_compile.AbstractHelper;

/**
 * 生成一份.java檔的必要資訊
 * 1. package
 * 2. type spec
 *
 * 可選資訊
 * 1. comment
 *
 */
public class FileHelper extends AbstractHelper<JavaFile, JavaFile.Builder> {


    private FileHelper(String pkg, TypeHelper type) {
        builder = JavaFile.builder(pkg, type.create());
    }

    public static FileHelper build(String pkg, TypeHelper type){
        return new FileHelper(pkg, type);
    }

    public FileHelper addComment(String format, Object... objs){
        builder.addFileComment(format, objs);
        return this;
    }

    @Override public JavaFile create() {
        return builder.build();
    }
}
