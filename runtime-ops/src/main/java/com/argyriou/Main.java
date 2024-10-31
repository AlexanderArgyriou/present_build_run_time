package com.argyriou;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

public class Main {
    public static void main(String[] args) {
        Reflections reflections =
                new Reflections("com", Scanners.TypesAnnotated);

        System.out.println(
                reflections.getTypesAnnotatedWith(Deprecated.class)
        );
    }
}