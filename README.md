# SimpleProcessor
![](https://img.shields.io/badge/language-java-orange.svg) 
![](https://img.shields.io/badge/license-apache%202.0-green.svg)
![](https://img.shields.io/badge/latest-1.0.1-blue.svg)
![](https://img.shields.io/badge/jdk-1.8-yellow.svg)

A library to simplify apt procedure.

---

## Download
#### Maven
```maven
<dependency>
  <groupId>org.arxing</groupId>
  <artifactId>simple-processor</artifactId>
  <version>latest_version</version>
  <type>pom</type>
</dependency>
```
#### gradle
```gradle
compile 'org.arxing:simple-processor:latest_version'
```

## Usage
1. Create two *java library* module in your project, one for your annotations, one for processors.
2. Define your custom annotation in annotations module.

```java
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface DemoAnnotation {
    String value() default "";
}
```
> `@Retention(RetentionPolicy.CLASS)` is mandatory.

3. Create a class extended *SimpleProcessor*.

```java
@AutoService(Processor.class)
public class DemoProcessor extends SimpleProcessor {
    @Override public Set<Class<? extends Annotation>> defineSupportedTypes() {
        return null;
    }

    @Override public String fileNameSuffix() {
        return null;
    }

    @Override public AnnotationHandler getHandler(Annotation annotation, String host, JavaFileHelper fileHelper) {
        return null;
    }
}
```
> `@AutoService(Processor.class)` is mandatory. It will auto create a file *javax.annotation.processing.Processor*
in *app/build/classes/java/main/META-INF/services*.

4. Implement your processor.

```java
@AutoService(Processor.class)
public class DemoProcessor extends SimpleProcessor {
    @Override public Set<Class<? extends Annotation>> defineSupportedTypes() {
        return new HashSet<>(Arrays.asList(DemoAnnotation.class));
    }

    @Override public String fileNameSuffix() {
        return "_Suffix";
    }

    @Override public AnnotationHandler getHandler(Annotation annotation, String host, JavaFileHelper fileHelper) {
        if(annotation instanceof DemoAnnotation)
            return new DemoHandler(annotation, host);
        return null;
    }

    private class DemoHandler extends AnnotationHandler<DemoAnnotation>{

        DemoHandler(Annotation annotation, String hostKey) {
            super(annotation, hostKey);
        }

        @Override public void putElement(VariableElement elField, TypeElement elHost) {

        }
    }
}
```
> Define your annotations in `1defineSupportedTypes()`.

> Build your AnnotationHandler<T> and return it in `getHandler()`.

> `host` is a full class name means which class owned this annoation.
> Example:
> There is a class defined a annotation.
> ```java
> package com.example.demo;
> public class MainActivity{
>     @DemoAnnotation("test") public int a;
> }
> ```
> When processor scanned this annotation, its `host` was 'com.example.demo.MainActivity'.

5. Implement your annotation handler.




