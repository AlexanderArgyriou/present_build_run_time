package com.argyriou.buildtime;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class DummyAugmentor {
    public static void main(String[] args) throws IOException {
        Reflections reflections =
                new Reflections("com", Scanners.TypesAnnotated);

        Set<Class<?>> annotatedClasses =
                reflections.getTypesAnnotatedWith(Deprecated.class);

        MethodSpec.Builder mainBuilder = MethodSpec.methodBuilder("main")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addParameter(String[].class, "args")
                .addStatement("$T<$T<?>> set = new $T<>()",
                        Set.class, Class.class, HashSet.class);

        for (Class<?> clazz : annotatedClasses) {
            mainBuilder.addStatement("set.add($T.class)", clazz);
        }

        mainBuilder.addStatement("$T.out.println(set)", System.class);

        TypeSpec mainClass = TypeSpec.classBuilder("Main")
                .addModifiers(Modifier.PUBLIC)
                .addMethod(mainBuilder.build())
                .build();

        JavaFile javaFile = JavaFile.builder("com.argyriou.buildtime", mainClass)
                .build();

        javaFile.writeTo(Paths.get("target/generated-sources/java"));
    }
}
